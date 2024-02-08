package ru.taratonov.dealkotlin.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

data class CreditDto(
    @field:Schema(
        description = "amount of the loan",
        name = "amount",
        example = "10000"
    )
    val amount: BigDecimal? = null,

    @field:Schema(
        description = "loan term",
        name = "term",
        example = "7"
    )
    val term: Int? = null,

    @field:Schema(
        description = "monthly payment of the loan",
        name = "monthlyPayment",
        example = "1245.67"
    )
    val monthlyPayment: BigDecimal? = null,

    @field:Schema(
        description = "loan rate",
        name = "rate",
        example = "3"
    )
    val rate: BigDecimal? = null,

    @field:Schema(
        description = "full cost of the loan",
        name = "psk",
        example = "2.56"
    )
    val psk: BigDecimal? = null,

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
    val isSalaryClient: Boolean? = null,

    @field:Schema(
        description = "list of payments",
        name = "paymentSchedule"
    )
    val paymentSchedule: List<PaymentScheduleElement>? = null
)
