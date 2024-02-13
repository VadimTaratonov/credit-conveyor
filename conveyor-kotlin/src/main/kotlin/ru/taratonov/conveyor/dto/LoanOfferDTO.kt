package ru.taratonov.conveyor.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import java.math.BigDecimal

data class LoanOfferDTO(
    @field:Schema(
        description = "id of application",
        name = "applicationId",
        example = "165464"
    )
    @field:Min(value = 1, message = "must be greater or equal than 1")
    val applicationId: Long? = null,

    @field:Schema(
        description = "the amount requested by the client",
        name = "requestedAmount",
        example = "10000"
    )
    @field:DecimalMin(value = "10000.0", message = "must be greater or equal than 10000")
    val requestedAmount: BigDecimal? = null,

    @field:Schema(
        description = "total amount of the loan",
        name = "totalAmount",
        example = "10000"
    )
    @field:DecimalMin(value = "10000.0", message = "must be greater or equal than 10000")
    val totalAmount: BigDecimal? = null,

    @field:Schema(
        description = "loan term",
        name = "term",
        example = "4"
    )
    @field:Min(value = 6, message = "must be greater or equal than 6")
    val term: Int? = null,

    @field:Schema(
        description = "monthly payment of the loan",
        name = "monthlyPayment",
        example = "1245.67"
    )
    @field:DecimalMin(value = "1", message = "must be greater or equal than 1")
    val monthlyPayment: BigDecimal? = null,

    @field:Schema(
        description = "loan rate",
        name = "rate",
        example = "3"
    )
    @field:DecimalMin(value = "0.1", message = "must be greater or equal than 0.1")
    val rate: BigDecimal? = null,

    @field:Schema(
        description = "availability of credit insurance",
        name = "isInsuranceEnabled",
        example = "false"
    )
    val isInsuranceEnabled: Boolean? = null,

    @field:Schema(
        description = "salary client",
        name = "isSalaryClient",
        example = "true"
    )
    val isSalaryClient: Boolean? = null
)
