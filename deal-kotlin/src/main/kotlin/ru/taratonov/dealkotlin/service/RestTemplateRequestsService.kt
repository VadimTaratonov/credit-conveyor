package ru.taratonov.dealkotlin.service

import ru.taratonov.dealkotlin.dto.CreditDto
import ru.taratonov.dealkotlin.dto.LoanApplicationRequestDto
import ru.taratonov.dealkotlin.dto.LoanOfferDto
import ru.taratonov.dealkotlin.dto.ScoringDataDto

interface RestTemplateRequestsService {
    fun requestToGetOffers(loanApplicationRequest: LoanApplicationRequestDto) : List<LoanOfferDto>
    fun requestToCalculateCredit(scoringData: ScoringDataDto): CreditDto
}