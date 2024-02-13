package ru.taratonov.conveyor.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.taratonov.conveyor.dto.LoanApplicationRequestDTO
import java.math.BigDecimal

class OfferServiceTest {

    private val creditCalculationService = mockk<CreditCalculationService>()
    private val scoringService = mockk<ScoringService>()
    private val nonValueString = "Should not be null"
    private val offerService = OfferServiceImpl(
        creditCalculationService = creditCalculationService,
        scoringService = scoringService,
        NON_VALUE = nonValueString
    )


    @Test
    fun `test amount of offers when calling method`() {
        val loanApplicationRequest = LoanApplicationRequestDTO(
            amount = BigDecimal.ONE,
            term = 1
        )

        every { scoringService.scoringPerson(any(), any()) } returns BigDecimal.ONE
        every { creditCalculationService.calculateTotalAmount(any(), any()) } returns BigDecimal.ONE
        every { creditCalculationService.calculateMonthlyPayment(any(), any(), any()) } returns BigDecimal.ONE

        val offers = offerService.createOffers(loanApplicationRequest)
        val actual = offers.size
        val expected = 4

        assertEquals(expected, actual)
        verify(exactly = expected) { scoringService.scoringPerson(any(), any()) }
        verify(exactly = expected) { creditCalculationService.calculateTotalAmount(any(), any()) }
        verify(exactly = expected) { creditCalculationService.calculateMonthlyPayment(any(), any(), any()) }
    }

    @Test
    fun `test create offers methods with correct data`() {
        val amount = BigDecimal.valueOf(10000)
        val term = 5
        val loanApplicationRequest = LoanApplicationRequestDTO(
            amount = amount,
            term = term
        )
        val rate = BigDecimal.valueOf(5)
        val totalAmountInsurance = BigDecimal.valueOf(11000)
        val monthlyPayment = BigDecimal.valueOf(1000)

        every { scoringService.scoringPerson(any(), any()) } returns rate
        every { creditCalculationService.calculateTotalAmount(any(), true) } returns totalAmountInsurance
        every { creditCalculationService.calculateTotalAmount(any(), false) } returns amount
        every { creditCalculationService.calculateMonthlyPayment(any(), any(), any()) } returns monthlyPayment

        val offers = offerService.createOffers(loanApplicationRequest)
        val offer = offers[0]

        assertEquals(loanApplicationRequest.amount, offer.requestedAmount)
        assertEquals(loanApplicationRequest.term, offer.term)
        assertEquals(rate, offer.rate)
        assertEquals(monthlyPayment, offer.monthlyPayment)
        assertEquals(amount, offers[0].totalAmount)
        assertEquals(amount, offers[1].totalAmount)
        assertEquals(totalAmountInsurance, offers[2].totalAmount)
        assertEquals(totalAmountInsurance, offers[3].totalAmount)
    }
}