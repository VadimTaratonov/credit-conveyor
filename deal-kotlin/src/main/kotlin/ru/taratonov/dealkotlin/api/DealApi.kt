package ru.taratonov.dealkotlin.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import ru.taratonov.dealkotlin.dto.ApplicationDto
import ru.taratonov.dealkotlin.dto.ErrorDto
import ru.taratonov.dealkotlin.dto.FinishRegistrationRequestDto
import ru.taratonov.dealkotlin.dto.LoanApplicationRequestDto
import ru.taratonov.dealkotlin.dto.LoanOfferDto

@RequestMapping("/deal")
@Tag(name = "Deal Controller", description = "Managing loan offers with using db")
interface DealApi {

    @PostMapping("/application")
    @Operation(summary = "Get loan offers", description = "Allows to get 4 loan offers for person")
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
        )
        @RequestBody
        @Valid loanApplicationRequest: LoanApplicationRequestDto
    ): List<LoanOfferDto>


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
    @ApiResponse(
        responseCode = "404",
        description = "Application not found",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorDto::class))]
    )
    fun getOneOfTheOffers(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Selected loan offer",
            content = [Content(schema = Schema(implementation = LoanOfferDto::class))]
        )
        @RequestBody
        @Valid loanOffer: LoanOfferDto
    ): ResponseEntity<HttpStatus>

    @PutMapping("/calculate/{applicationId}")
    @Operation(summary = "Get loan parameters", description = "Allows to get all parameters for credit")
    @ApiResponse(
        responseCode = "200",
        description = "Parameters received!",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ResponseEntity::class))]
    )
    @ApiResponse(
        responseCode = "400",
        description = "Scoring failed",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorDto::class))]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Application not found",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorDto::class))]
    )
    fun calculateCredit(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Finish registration request",
            content = [Content(schema = Schema(implementation = FinishRegistrationRequestDto::class))]
        )
        @RequestBody
        @Valid finishRegistrationRequest: FinishRegistrationRequestDto,
        @Parameter(description = "Id of the application", required = true)
        @PathVariable("applicationId") id: Long
    ): ResponseEntity<HttpStatus>

    @GetMapping("/applicationDto/{id}")
    @Operation(summary = "Get applicationDto", description = "Allows to get applicationDto")
    @ApiResponse(
        responseCode = "200",
        description = "Application received!",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ApplicationDto::class))]
    )
    @ApiResponse(
        responseCode = "404",
        description = "Application not found",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorDto::class))]
    )
    fun getApplicationDTOById(
        @Parameter(description = "Id of the application", required = true)
        @PathVariable id: Long
    ): ApplicationDto
}