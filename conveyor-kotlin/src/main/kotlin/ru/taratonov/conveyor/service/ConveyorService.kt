package ru.taratonov.conveyor.service

import ru.taratonov.conveyor.dto.CreditDTO
import ru.taratonov.conveyor.dto.LoanApplicationRequestDTO
import ru.taratonov.conveyor.dto.LoanOfferDTO
import ru.taratonov.conveyor.dto.ScoringDataDTO

interface ConveyorService {
    fun getOffers(loanApplicationRequest: LoanApplicationRequestDTO): List<LoanOfferDTO>

    fun calculateParameters(scoringData: ScoringDataDTO): CreditDTO
}