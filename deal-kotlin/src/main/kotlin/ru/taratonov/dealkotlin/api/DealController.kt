package ru.taratonov.dealkotlin.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import ru.taratonov.dealkotlin.dto.ApplicationDto
import ru.taratonov.dealkotlin.dto.FinishRegistrationRequestDto
import ru.taratonov.dealkotlin.dto.LoanApplicationRequestDto
import ru.taratonov.dealkotlin.dto.LoanOfferDto
import ru.taratonov.dealkotlin.service.DealService

@RestController
class DealController(
    private val dealService: DealService
): DealApi  {
    override fun getPossibleLoanOffers(loanApplicationRequest: LoanApplicationRequestDto): List<LoanOfferDto> {
        return dealService.getOffers(loanApplicationRequest)
    }

    override fun getOneOfTheOffers(loanOffer: LoanOfferDto): ResponseEntity<HttpStatus> {
        dealService.chooseOffer(loanOffer)
        return ResponseEntity.ok(HttpStatus.OK)
    }

    override fun calculateCredit(
        finishRegistrationRequest: FinishRegistrationRequestDto,
        id: Long
    ): ResponseEntity<HttpStatus> {
        dealService.calculateCredit(finishRegistrationRequest, id)
        return ResponseEntity.ok(HttpStatus.OK)
    }

    override fun getApplicationDTOById(id: Long): ApplicationDto {
        return dealService.getApplicationDTOById(id)
    }
}