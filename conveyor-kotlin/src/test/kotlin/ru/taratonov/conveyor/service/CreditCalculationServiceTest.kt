package ru.taratonov.conveyor.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.taratonov.conveyor.dto.ScoringDataDTO
import java.math.BigDecimal
import java.math.RoundingMode

@SpringBootTest
class CreditCalculationServiceTest {

    @Autowired
    private lateinit var creditCalculationService: CreditCalculationServiceImpl

    @Test
    fun `test calculate loan parameters method with rate less than zero`() {
        val rate = BigDecimal.valueOf(-1)
        val scoringData = ScoringDataDTO(
            isInsuranceEnabled = true,
            isSalaryClient = true,
            amount = BigDecimal.ONE,
            term = 1
        )
        assertThrows(IllegalArgumentException::class.java) {
            creditCalculationService.calculateLoanParameters(scoringData, rate)
        }
    }

    @Test
    fun `test calculate loan parameters method with correct data`() {
        val newRate = BigDecimal.valueOf(5)
        val scoringData = ScoringDataDTO(
            isInsuranceEnabled = false,
            isSalaryClient = true,
            amount = BigDecimal.valueOf(10000),
            term = 1
        )
        val creditDTO = creditCalculationService.calculateLoanParameters(scoringData, newRate)

        assertEquals(scoringData.term, creditDTO.term)
        assertEquals(scoringData.isInsuranceEnabled, creditDTO.isInsuranceEnabled)
        assertEquals(scoringData.isSalaryClient, creditDTO.isSalaryClient)
        assertEquals(scoringData.amount, creditDTO.amount)
    }

    @Test
    fun `test calculate monthly payment method with parameters less than zero`() {
        assertThrows(IllegalArgumentException::class.java) {
            val amount = BigDecimal.valueOf(-1).setScale(2,RoundingMode.CEILING)
            val rate = BigDecimal.valueOf(5)
            val term = 6
            creditCalculationService.calculateMonthlyPayment(amount, rate, term)
        }

        assertThrows(IllegalArgumentException::class.java) {
            val amount = BigDecimal.valueOf(10000).setScale(2,RoundingMode.CEILING)
            val rate = BigDecimal.valueOf(-1)
            val term = 6
            creditCalculationService.calculateMonthlyPayment(amount, rate, term)
        }

        assertThrows(IllegalArgumentException::class.java) {
            val amount = BigDecimal.valueOf(10000).setScale(2,RoundingMode.CEILING)
            val rate = BigDecimal.valueOf(5)
            val term = -1
            creditCalculationService.calculateMonthlyPayment(amount, rate, term)
        }
    }

    @Test
    fun `test calculate monthly payment method with zero amount`() {
        val amount = BigDecimal.valueOf(0).setScale(2, RoundingMode.CEILING)
        val rate = BigDecimal.valueOf(5)
        val term = 6

        val actual = creditCalculationService.calculateMonthlyPayment(amount, rate, term)
        val expected = BigDecimal.valueOf(0).setScale(2, RoundingMode.CEILING)

        assertEquals(expected, actual)
    }

    @Test
    fun `test calculate monthly payment method with non zero amount`() {
        val amount = BigDecimal.valueOf(10000).setScale(2, RoundingMode.CEILING)
        val rate = BigDecimal.valueOf(5)
        val term = 6

        val actual = creditCalculationService.calculateMonthlyPayment(amount, rate, term)
        val expected = BigDecimal.valueOf(1691.06).setScale(2, RoundingMode.CEILING)

        assertEquals(expected, actual)
    }

    @Test
    fun `test calculate payment schedule method with parameters less than zero`() {
        assertThrows(IllegalArgumentException::class.java) {
            val amount = BigDecimal.valueOf(-1).setScale(2,RoundingMode.CEILING)
            val rate = BigDecimal.valueOf(5)
            val term = 6
            creditCalculationService.calculatePaymentSchedule(amount, rate, term)
        }

        assertThrows(IllegalArgumentException::class.java) {
            val amount = BigDecimal.valueOf(10000).setScale(2,RoundingMode.CEILING)
            val rate = BigDecimal.valueOf(-1)
            val term = 6
            creditCalculationService.calculatePaymentSchedule(amount, rate, term)
        }

        assertThrows(IllegalArgumentException::class.java) {
            val amount = BigDecimal.valueOf(10000).setScale(2,RoundingMode.CEILING)
            val rate = BigDecimal.valueOf(5)
            val term = -1
            creditCalculationService.calculatePaymentSchedule(amount, rate, term)
        }
    }

    @Test
    fun `test calculate payment schedule method and check size of result list`(){
        val amount = BigDecimal.valueOf(10000).setScale(2, RoundingMode.CEILING)
        val rate = BigDecimal.valueOf(5)
        val term = 6

        val actual = creditCalculationService.calculatePaymentSchedule(amount, rate, term).size

        assertEquals(term, actual)
    }

    @Test
    fun `test calculate payment schedule method and check result parameters of first payment`(){
        val amount = BigDecimal.valueOf(10000).setScale(2, RoundingMode.CEILING)
        val rate = BigDecimal.valueOf(5)
        val term = 6

        val actual = creditCalculationService.calculatePaymentSchedule(amount, rate, term)
        val paymentScheduleElement = actual[0]

        val interestPaymentExpected = BigDecimal.valueOf(42.35).setScale(2, RoundingMode.CEILING)
        val debtPaymentExpected = BigDecimal.valueOf(1648.71).setScale(2, RoundingMode.CEILING)
        val remainingDebtExpected = BigDecimal.valueOf(8351.29).setScale(2, RoundingMode.CEILING)

        assertEquals(paymentScheduleElement.interestPayment, interestPaymentExpected)
        assertEquals(paymentScheduleElement.debtPayment, debtPaymentExpected)
        assertEquals(paymentScheduleElement.remainingDebt, remainingDebtExpected)
    }

    @Test
    fun `test calculate total amount method where insurance is enabled`() {
        val isInsuranceEnabled = true
        val amount = BigDecimal.valueOf(1000)

        val actual = creditCalculationService.calculateTotalAmount(amount, isInsuranceEnabled)

        val expected = BigDecimal.valueOf(1100).setScale(2, RoundingMode.CEILING)

        assertEquals(expected, actual)
    }

    @Test
    fun `test calculate total amount method where insurance is not enabled`() {
        val isInsuranceEnabled = false
        val amount = BigDecimal.valueOf(1000)

        val actual = creditCalculationService.calculateTotalAmount(amount, isInsuranceEnabled)

        assertEquals(amount, actual)
    }

    @Test
    fun `test calculate total amount method with parameters less than zero`() {
        assertThrows(IllegalArgumentException::class.java)  {
            val amount = BigDecimal.valueOf(-1).setScale(2, RoundingMode.CEILING)
            val isInsuranceEnabled = false
            creditCalculationService.calculateTotalAmount(amount, isInsuranceEnabled)
        }
    }
}