package ru.taratonov.conveyor.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.taratonov.conveyor.dto.CreditDTO
import ru.taratonov.conveyor.dto.PaymentScheduleElement
import ru.taratonov.conveyor.dto.ScoringDataDTO
import ru.taratonov.conveyor.util.error
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.Year
import java.util.*

@Service
class CreditCalculationServiceImpl(
    @Value("\${custom.insurance.percentage}") private val INSURANCE_PERCENTAGE: BigDecimal,
    @Value("\${custom.text.invalidValue}") private val INVALID_VALUE: String,
    @Value("\${custom.text.nonNull}") private val NON_VALUE: String
) : CreditCalculationService {

    private val calendar: Calendar = Calendar.getInstance()

    private val logger = KotlinLogging.logger { }

    /**
     * Вычисление необходимых полей для заявки клиента
     **/
    override fun calculateLoanParameters(scoringData: ScoringDataDTO, newRate: BigDecimal): CreditDTO {
        checkArgumentsOfLoan(rate = newRate)

        val isInsuranceEnabled = scoringData.isInsuranceEnabled ?: error(NON_VALUE)
        val amount = calculateTotalAmount(
            scoringData.amount ?: error(NON_VALUE),
            isInsuranceEnabled
        )
        val term = scoringData.term ?: error(NON_VALUE)
        val paymentSchedule = calculatePaymentSchedule(amount, newRate, term)

        return CreditDTO(
            amount = amount,
            term = term,
            monthlyPayment = calculateMonthlyPayment(amount, newRate, term),
            rate = newRate,
            psk = calculatePSK(amount, term, paymentSchedule),
            isInsuranceEnabled = isInsuranceEnabled,
            isSalaryClient = scoringData.isSalaryClient ?: error(NON_VALUE),
            paymentSchedule = paymentSchedule
        )
    }

    /**
     * Вычисление ежемесячного платежа
     * ЕП = СК * КА
     * ЕП - ежемесячный платеж
     * СК - сумма кредита
     * КА - коэффициент аннуитета
     * КА = (МПС * (1 + МПС)^КП)/((1 + МПС)^КП - 1)
     * МПС - месячная процентная ставка
     * КП - количество платежей
     */
    override fun calculateMonthlyPayment(amount: BigDecimal, rate: BigDecimal, term: Int): BigDecimal {
        logger.debug("!START CALCULATE MONTHLY PAYMENT WITH AMOUNT - {}, RATE - {}, TERM - {}!", amount, rate, term)

        checkArgumentsOfLoan(amount, rate, term)

        // Месячная процентная ставка
        val monthlyRate = rate
            .divide(BigDecimal.valueOf(100), 6, RoundingMode.CEILING)
            .divide(BigDecimal.valueOf(12), 6, RoundingMode.CEILING)
        logger.debug("Monthly rate is {}", monthlyRate)

        // Промежуточное вычисление скобки в выражении
        val annuityCalculationBracket = (BigDecimal.ONE.add(monthlyRate))
            .pow(term)
            .setScale(6, RoundingMode.CEILING)

        // Коэффициент аннуитета
        val annuityRatio = (annuityCalculationBracket.multiply(monthlyRate))
            .divide(annuityCalculationBracket.subtract(BigDecimal.ONE), 6, RoundingMode.CEILING)
        logger.debug("Annuity ration is {}", annuityRatio)

        // Ежемесячный платеж
        val monthlyPayment = amount.multiply(annuityRatio).setScale(2, RoundingMode.CEILING)
        logger.debug("!FINISH CALCULATE MONTHLY PAYMENT. RESULT IS {}!", monthlyPayment)

        return monthlyPayment
    }

    /**
     * Вычисление графиков платежей
     * */
    override fun calculatePaymentSchedule(
        amount: BigDecimal,
        rate: BigDecimal,
        term: Int
    ): List<PaymentScheduleElement> {
        logger.debug("!START CALCULATE PAYMENT SCHEDULE WITH AMOUNT - {}, TERM - {},  RATE - {}!", amount, term, rate)

        checkArgumentsOfLoan(amount, rate, term)

        val paymentSchedule = mutableListOf<PaymentScheduleElement>()
        var paymentDate = LocalDate.now()
        var remainingDebt = amount
        val monthlyPayment = calculateMonthlyPayment(amount, rate, term)

        for (i in 1..term) {
            paymentDate = paymentDate.plusMonths(1)
            calendar[paymentDate.year, paymentDate.monthValue - 2] = 1
            val numOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val numOfDaysInYear = Year.of(paymentDate.year).length()

            val interestPayment = calculateInterestPayment(remainingDebt, rate, numOfDaysInMonth, numOfDaysInYear)
            val debtPayment = calculateDebtPayment(monthlyPayment, interestPayment)
            remainingDebt = calculateRemainingDebt(remainingDebt, debtPayment)

            paymentSchedule.add(
                PaymentScheduleElement(i, paymentDate, monthlyPayment, interestPayment, debtPayment, remainingDebt)
            )
            logger.info(
                "{} payment will be made on {}, monthly payment - {}, interest payment - {}, debt payment - {}, remaining debt - {}",
                i, paymentDate, monthlyPayment, interestPayment, debtPayment, remainingDebt
            )

        }
        logger.debug("!FINISH CALCULATE PAYMENT SCHEDULE!")
        return paymentSchedule
    }

    /**
     * Вычисление ПСК
     * ПСК = (СП / СК - 1) / N
     * ПСК - полная стоимость кредита
     * СП - сумма платежей
     * СК - сумма кредита
     * N - продолжительность кредита в годах
     */
    override fun calculatePSK(
        amount: BigDecimal,
        term: Int,
        scheduleElements: List<PaymentScheduleElement>
    ): BigDecimal {
        logger.debug("!START CALCULATE PSK WITH AMOUNT - {},TERM - {} AND PAYMENT SCHEDULE!", amount, term)

        val yearTerm = BigDecimal.valueOf(term.toLong()).divide(BigDecimal.valueOf(12), 2, RoundingMode.CEILING)
        var totalAmount = scheduleElements
            .map { it.totalPayment ?: error(NON_VALUE) }
            .reduce { x, y -> x.add(y) }

        totalAmount = totalAmount
            .add(scheduleElements[scheduleElements.size - 1].remainingDebt)
            .setScale(0, RoundingMode.CEILING)
        logger.debug("total amount for paying loan is {}", totalAmount)

        val psk = totalAmount
            .divide(amount, 6, RoundingMode.CEILING)
            .subtract(BigDecimal.ONE)
            .divide(yearTerm, 6, RoundingMode.CEILING)
            .multiply(BigDecimal.valueOf(100))
            .setScale(3, RoundingMode.CEILING)
        logger.debug("!FINISH CALCULATE PSK.")
        logger.info("Result psk  is {}!", psk)

        return psk
    }

    /**
     * Вычисление полной стоимости кредита с учетом страховки
     * ПС = СК + СС
     * ПС - полная стоимость
     * СК - сумма кредита
     * СС - сумма страховки
     */
    override fun calculateTotalAmount(amount: BigDecimal, isInsuranceEnabled: Boolean): BigDecimal {
        logger.debug(
            "!START CALCULATE BASE TOTAL AMOUNT WITH AMOUNT - {}, isInsuranceEnabled - {}!",
            amount,
            isInsuranceEnabled
        )

        checkArgumentsOfLoan(amount = amount)
        var resultAmount = amount
        if (isInsuranceEnabled) {
            resultAmount = amount
                .add(amount.multiply(INSURANCE_PERCENTAGE.divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING)))
        }
        logger.debug("!FINISH CALCULATE BASE TOTAL AMOUNT!")
        logger.info("Total amount is {}!", resultAmount)

        return resultAmount
    }

    /**
     * Вычисление процентов по платежу
     * ПК = ОД * ПС * КДМ * КДГ
     * ПК - проценты по кредиту
     * ОД - остаток долга
     * ПС - процентная ставка
     * КДМ - количество дней в месяце
     * КДГ - количество дней в году
     */
    private fun calculateInterestPayment(
        remainingDebt: BigDecimal,
        rate: BigDecimal,
        numOfDaysInMonth: Int,
        numOfDaysInYear: Int
    ): BigDecimal {
        logger.debug(
            "!START CALCULATE INTEREST PAYMENT WITH remainingDebt - {}, rate - {}, numOfDaysInMonth - {}, " +
                    "numOfDaysInYear - {}!", remainingDebt, rate, numOfDaysInMonth, numOfDaysInYear
        )

        checkArgumentsOfLoan(amount = remainingDebt, rate = rate)
        checkArgumentsOfDate(numOfDaysInMonth, numOfDaysInYear)

        val rateValue = rate.divide(BigDecimal.valueOf(100), 6, RoundingMode.CEILING)
        val interestPayment = remainingDebt
            .multiply(rateValue)
            .multiply(BigDecimal.valueOf(numOfDaysInMonth.toLong()))
            .divide(BigDecimal.valueOf(numOfDaysInYear.toLong()), 2, RoundingMode.CEILING)
        logger.debug("!FINISH CALCULATE INTEREST PAYMENT. RESULT IS {}!", interestPayment)

        return interestPayment
    }

    /**
     * Вычисление суммы тела кредита
     * ТК = ЕП - ПК
     * ТК - тело кредита
     * ЕП - ежемесячный платеж
     * ПК - проценты по кредиту
     */
    private fun calculateDebtPayment(totalPayment: BigDecimal, interestPayment: BigDecimal): BigDecimal {
        logger.debug(
            "!START CALCULATE DEBT PAYMENT WITH totalPayment - {}, interestPayment - {}!",
            totalPayment, interestPayment
        )

        checkArgumentsOfLoan(totalPayment, interestPayment)

        val debtPayment = totalPayment
            .subtract(interestPayment)
            .setScale(2, RoundingMode.CEILING)
        logger.debug("!FINISH CALCULATE DEBT PAYMENT. RESULT IS {}!", debtPayment)

        return debtPayment
    }

    /**
     * Вычисление остаточной суммы долга
     * ОСД = СК - ТК
     * ОСД - остаточная сумма долга
     * СК - сумма кредита, который осталось выплатить
     * ТК - тело кредита
     */
    private fun calculateRemainingDebt(amount: BigDecimal, debtPayment: BigDecimal): BigDecimal {
        logger.debug(
            "!START CALCULATE REMAINING DEBT WITH amount - {}, debtPayment - {}!",
            amount, debtPayment
        )

        checkArgumentsOfLoan(amount, debtPayment)

        val remainingDebt = amount
            .subtract(debtPayment)
            .setScale(2, RoundingMode.CEILING)
        logger.debug("!FINISH CALCULATE REMAINING DEBT. RESULT IS {}!", remainingDebt)

        return remainingDebt
    }

    private fun checkArgumentsOfLoan(
        amount: BigDecimal = BigDecimal.ONE,
        rate: BigDecimal = BigDecimal.ONE,
        term: Int = 1
    ) {
        if (amount < BigDecimal.ZERO || rate < BigDecimal.ZERO || term < 0) {
            throw IllegalArgumentException(INVALID_VALUE)
        }
    }

    private fun checkArgumentsOfDate(
        numOfDaysInMonth: Int,
        numOfDaysInYear: Int
    ) {
        if (numOfDaysInMonth < 27 || numOfDaysInMonth > 31 || numOfDaysInYear < 365 || numOfDaysInYear > 366) {
            throw IllegalArgumentException(INVALID_VALUE)
        }
    }
}