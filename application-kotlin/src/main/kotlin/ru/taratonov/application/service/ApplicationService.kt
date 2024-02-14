package ru.taratonov.application.service

import ru.taratonov.application.dto.LoanApplicationRequestDto
import ru.taratonov.application.dto.LoanOfferDto

interface ApplicationService {
    fun getOffers(loanApplicationRequest: LoanApplicationRequestDto) : List<LoanOfferDto>

    fun chooseOffer(loanOffer : LoanOfferDto)
}