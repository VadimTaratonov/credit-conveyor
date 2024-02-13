package ru.taratonov.conveyor.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.taratonov.conveyor.dto.CreditDTO
import ru.taratonov.conveyor.dto.LoanApplicationRequestDTO
import ru.taratonov.conveyor.dto.LoanOfferDTO
import ru.taratonov.conveyor.dto.ScoringDataDTO
import java.math.BigDecimal

class ConveyorServiceTest {

    private val scoringService = mockk<ScoringService>()
    private val creditCalculationService = mockk<CreditCalculationService>()
    private val offerService = mockk<OfferService>()
    private val conveyorService = ConveyorServiceImpl(
        offerService = offerService,
        scoringService = scoringService,
        creditCalculationService = creditCalculationService
    )


    @Test
    fun getOffers() {
        every { offerService.createOffers(any()) } returns listOf(LoanOfferDTO(), LoanOfferDTO())

        val loanApplicationRequestDTO = LoanApplicationRequestDTO()

        val offers = conveyorService.getOffers(loanApplicationRequestDTO)

        Assertions.assertEquals(2, offers.size)
        verify(exactly = 1) { offerService.createOffers(loanApplicationRequestDTO) }
    }

    @Test
    fun calculateParameters() {
        val scoringDataDTO = ScoringDataDTO()
        val newRate = BigDecimal.ONE

        every { scoringService.scoringPerson(scoringDataDTO) } returns newRate
        every { creditCalculationService.calculateLoanParameters(scoringDataDTO, newRate) } returns CreditDTO()

        conveyorService.calculateParameters(scoringDataDTO)

        verify(exactly = 1) { scoringService.scoringPerson(scoringDataDTO) }
        verify(exactly = 1) { creditCalculationService.calculateLoanParameters(scoringDataDTO, newRate) }
    }
}