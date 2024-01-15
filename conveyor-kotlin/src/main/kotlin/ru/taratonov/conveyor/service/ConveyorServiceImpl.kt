package ru.taratonov.conveyor.service

import org.springframework.stereotype.Service
import ru.taratonov.conveyor.dto.CreditDTO
import ru.taratonov.conveyor.dto.LoanApplicationRequestDTO
import ru.taratonov.conveyor.dto.LoanOfferDTO
import ru.taratonov.conveyor.dto.ScoringDataDTO

@Service
class ConveyorServiceImpl(
    private val scoringService: ScoringService,
    private val creditCalculationService: CreditCalculationService,
    private val offerService: OfferService
) : ConveyorService {
    override fun getOffers(loanApplicationRequest: LoanApplicationRequestDTO): List<LoanOfferDTO> {
        return offerService.createOffers(loanApplicationRequest)
    }

    override fun calculateParameters(scoringData: ScoringDataDTO): CreditDTO {
        return creditCalculationService
            .calculateLoanParameters(scoringData, scoringService.scoringPerson(scoringData))
    }

}