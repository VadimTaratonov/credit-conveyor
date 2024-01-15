package ru.taratonov.conveyor.service

import ru.taratonov.conveyor.dto.CreditDTO
import ru.taratonov.conveyor.dto.PaymentScheduleElement
import ru.taratonov.conveyor.dto.ScoringDataDTO
import java.math.BigDecimal

interface CreditCalculationService {
    /**
     * Вычисление необходимых полей для заявки клиента
     * */
    fun calculateLoanParameters(scoringData: ScoringDataDTO, newRate: BigDecimal): CreditDTO

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
    fun calculateMonthlyPayment(amount: BigDecimal, rate: BigDecimal, term: Int): BigDecimal

    /**
     * Вычисление графиков платежей
     * */
    fun calculatePaymentSchedule(amount: BigDecimal, rate: BigDecimal, term: Int): List<PaymentScheduleElement>

    /**
     * Вычисление ПСК
     * ПСК = (СП / СК - 1) / N
     * ПСК - полная стоимость кредита
     * СП - сумма платежей
     * СК - сумма кредита
     * N - продолжительность кредита в годах
     */
    fun calculatePSK(amount: BigDecimal, term: Int, scheduleElements: List<PaymentScheduleElement>): BigDecimal

    /**
     * Вычисление полной стоимости кредита с учетом страховки
     * ПС = СК + СС
     * ПС - полная стоимость
     * СК - сумма кредита
     * СС - сумма страховки
     */
    fun calculateTotalAmount(amount: BigDecimal, isInsuranceEnabled: Boolean): BigDecimal
}