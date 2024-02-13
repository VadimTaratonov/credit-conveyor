package ru.taratonov.conveyor.api

import org.springframework.web.bind.annotation.RestController
import ru.taratonov.conveyor.dto.CreditDTO
import ru.taratonov.conveyor.dto.LoanApplicationRequestDTO
import ru.taratonov.conveyor.dto.LoanOfferDTO
import ru.taratonov.conveyor.dto.ScoringDataDTO
import ru.taratonov.conveyor.service.ConveyorService

@RestController
class ConveyorController(
    private val conveyorService: ConveyorService
) : ConveyorApi {
    override fun getPossibleLoanOffers(loanApplicationRequest: LoanApplicationRequestDTO): List<LoanOfferDTO> {
        return conveyorService.getOffers(loanApplicationRequest)
    }

    override fun calculateLoanParameters(scoringData: ScoringDataDTO): CreditDTO {
        return conveyorService.calculateParameters(scoringData)
    }
}