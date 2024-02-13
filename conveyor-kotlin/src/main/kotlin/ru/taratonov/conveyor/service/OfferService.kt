package ru.taratonov.conveyor.service

import ru.taratonov.conveyor.dto.LoanApplicationRequestDTO
import ru.taratonov.conveyor.dto.LoanOfferDTO

interface OfferService {
    fun createOffers(loanApplicationRequest: LoanApplicationRequestDTO): List<LoanOfferDTO>
}