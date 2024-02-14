package ru.taratonov.application.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import ru.taratonov.application.dto.ErrorDto
import ru.taratonov.application.dto.LoanApplicationRequestDto
import ru.taratonov.application.dto.LoanOfferDto

@Tag(name = "Application Controller", description = "the Application API")
@RequestMapping("/application")
interface ApplicationApi {

    @PostMapping
    @Operation(
        summary = "Get loan offers",
        description = "Allows to get 4 loan offers for person"
    )
    @ApiResponse(
        responseCode = "200",
        description = "List of offers received!",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = LoanOfferDto::class))]
    )
    @ApiResponse(
        responseCode = "400",
        description = "Prescoring failed",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorDto::class))]
    )
    fun getPossibleLoanOffers(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Loan request",
            content = [Content(schema = Schema(implementation = LoanApplicationRequestDto::class))]
        ) @RequestBody @Valid loanApplicationRequest: LoanApplicationRequestDto
    ): ResponseEntity<List<LoanOfferDto>>


    @PutMapping("/offer")
    @Operation(summary = "Choose one offer", description = "Allows to choose one of four offers")
    @ApiResponse(
        responseCode = "200",
        description = "The offer is selected",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ResponseEntity::class))]
    )
    @ApiResponse(
        responseCode = "400",
        description = "Fail to choose offer",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorDto::class))]
    )
    fun getOneOfTheOffers(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Loan request",
            content = [Content(schema = Schema(implementation = LoanOfferDto::class))]
        ) @RequestBody @Valid loanOfferDTO: LoanOfferDto
    ): ResponseEntity<HttpStatus>
}