package ru.taratonov.dealkotlin.service

import ru.taratonov.dealkotlin.dto.CreditDto
import ru.taratonov.dealkotlin.dto.FinishRegistrationRequestDto
import ru.taratonov.dealkotlin.dto.LoanApplicationRequestDto
import ru.taratonov.dealkotlin.dto.LoanOfferDto
import ru.taratonov.dealkotlin.dto.ScoringDataDto
import ru.taratonov.dealkotlin.enums.ApplicationStatus
import ru.taratonov.dealkotlin.model.Application
import ru.taratonov.dealkotlin.model.Client
import ru.taratonov.dealkotlin.model.Credit

interface FillingDataService {
    fun createClientOfRequest(loanApplicationRequest: LoanApplicationRequestDto): Client

    fun createApplicationOfRequest(client: Client): Application

    fun updateApplicationWhenChooseOffer(application: Application, loanOffer: LoanOfferDto): Application

    fun fillAllInformationToScoringData(
        finishRegistrationRequest: FinishRegistrationRequestDto,
        client: Client,
        application: Application
    ): ScoringDataDto

    fun createCreditAfterCalculating(creditDto: CreditDto, application: Application): Credit

    fun fillAllDataOfClient(client: Client, finishRegistrationRequest: FinishRegistrationRequestDto): Client

    fun updateApplicationWithNewStatus(application: Application, applicationStatus: ApplicationStatus): Application
}