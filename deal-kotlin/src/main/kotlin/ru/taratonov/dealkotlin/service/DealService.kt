package ru.taratonov.dealkotlin.service

import ru.taratonov.dealkotlin.dto.ApplicationDto
import ru.taratonov.dealkotlin.dto.FinishRegistrationRequestDto
import ru.taratonov.dealkotlin.dto.LoanApplicationRequestDto
import ru.taratonov.dealkotlin.dto.LoanOfferDto

interface DealService {
    fun getOffers(loanApplicationRequest: LoanApplicationRequestDto): List<LoanOfferDto>
    fun chooseOffer(loanOffer: LoanOfferDto)
    fun calculateCredit(finishRegistrationRequest: FinishRegistrationRequestDto, id: Long)
    fun getApplicationDTOById(id: Long) : ApplicationDto
}