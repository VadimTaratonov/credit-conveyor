package ru.taratonov.application.service

import ru.taratonov.application.dto.LoanApplicationRequestDto
import ru.taratonov.application.dto.LoanOfferDto

interface RestTemplateRequestsService {
    fun requestToGetOffers(loanApplicationRequest: LoanApplicationRequestDto) : List<LoanOfferDto>

    fun chooseOffer(loanOffer:LoanOfferDto)
}