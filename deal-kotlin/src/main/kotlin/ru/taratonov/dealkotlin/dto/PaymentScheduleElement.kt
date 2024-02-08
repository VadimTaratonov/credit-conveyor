package ru.taratonov.dealkotlin.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate

data class PaymentScheduleElement(
    @field:Schema(
        description = "payment number",
        name = "number",
        example = "1"
    )
    val number: Int? = null,

    @field:Schema(
        description = "payment date",
        name = "date",
        example = "2023-12-11"
    )
    val date: LocalDate? = null,

    @field:Schema(
        description = "the full amount of the monthly payment",
        name = "totalPayment",
        example = "11000"
    )
    val totalPayment: BigDecimal? = null,

    @field:Schema(
        description = "the amount of the interest payment",
        name = "interestPayment",
        example = "1200"
    )
    val interestPayment: BigDecimal? = null,

    @field:Schema(
        description = "the amount to repay the loan body",
        name = "debtPayment",
        example = "5785"
    )
    val debtPayment: BigDecimal? = null,

    @field:Schema(
        description = "remaining debt",
        name = "remainingDebt",
        example = "235367"
    )
    val remainingDebt: BigDecimal? = null
)
