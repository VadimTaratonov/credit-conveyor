package ru.taratonov.conveyor.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import ru.taratonov.conveyor.dto.CreditDTO
import ru.taratonov.conveyor.dto.ErrorDTO
import ru.taratonov.conveyor.dto.LoanApplicationRequestDTO
import ru.taratonov.conveyor.dto.LoanOfferDTO
import ru.taratonov.conveyor.dto.ScoringDataDTO

@RequestMapping("/conveyor")
@Tag(name = "Conveyor Controller", description = "Managing loan offers")
interface ConveyorApi {

    @PostMapping("/offers")
    @Operation(summary = "Get loan offers", description = "Allows to get 4 loan offers for person")
    @ApiResponse(
        responseCode = "200",
        description = "List of offers received!",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = LoanOfferDTO::class))]
    )
    @ApiResponse(
        responseCode = "400",
        description = "Prescoring failed",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorDTO::class))]
    )
    fun getPossibleLoanOffers(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Loan request",
            content = [Content(schema = Schema(implementation = LoanApplicationRequestDTO::class))]
        )
        @RequestBody
        @Valid loanApplicationRequest: LoanApplicationRequestDTO
    ): List<LoanOfferDTO>

    @PostMapping("/calculation")
    @Operation(summary = "Get loan parameters", description = "Allows to get all parameters for credit")
    @ApiResponse(
        responseCode = "200",
        description = "Parameters received!",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = CreditDTO::class))]
    )
    @ApiResponse(
        responseCode = "400",
        description = "Scoring failed",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorDTO::class))]
    )
    fun calculateLoanParameters(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "All information about user for calculation loan parameters",
            content = [Content(schema = Schema(implementation = ScoringDataDTO::class))]
        )
        @RequestBody
        @Valid scoringData: ScoringDataDTO
    ): CreditDTO
}