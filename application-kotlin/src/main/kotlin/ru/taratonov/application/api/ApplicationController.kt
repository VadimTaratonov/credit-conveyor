package ru.taratonov.application.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.taratonov.application.dto.LoanApplicationRequestDto
import ru.taratonov.application.dto.LoanOfferDto
import ru.taratonov.application.service.ApplicationService

@RestController
class ApplicationController(
    val applicationService: ApplicationService
) : ApplicationApi {

    override fun getPossibleLoanOffers(loanApplicationRequest: LoanApplicationRequestDto): ResponseEntity<List<LoanOfferDto>> {
        return ResponseEntity.ok(applicationService.getOffers(loanApplicationRequest))
    }

    override fun getOneOfTheOffers(loanOfferDTO: LoanOfferDto): ResponseEntity<HttpStatus> {
        applicationService.chooseOffer(loanOfferDTO)
        return ResponseEntity.ok(HttpStatus.OK)
    }
}