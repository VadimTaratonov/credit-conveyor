package ru.taratonov.dealkotlin.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate

data class ApplicationDto(
    @field:Schema(
        description = "application id",
        name = "applicationId",
        example = "1"
    )
    val applicationId: Long? = null,

    @field:Schema(
        description = "first name of person",
        name = "firstName",
        example = "Vadim"
    )
    val firstName: String? = null,

    @field:Schema(
        description = "last name of person",
        name = "lastName",
        example = "Taratonov"
    )
    val lastName: String? = null,

    @field:Schema(
        description = "middle name of person",
        name = "middleName",
        example = "Nikolaevich"
    )
    val middleName: String? = null,

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
        description = "list of payments",
        name = "paymentSchedule"
    )
    val paymentSchedule: List<PaymentScheduleElement>? = null,

    @field:Schema(
        description = "availability of credit insurance",
        name = "insuranceEnable",
        example = "false"
    )
    val insuranceEnable: Boolean? = null,

    @field:Schema(
        description = "salary client",
        name = "salaryClient",
        example = "true"
    )
    val salaryClient: Boolean? = null,

    @field:Schema(
        description = "day of creation loan request",
        name = "creationDate",
        example = "2023-02-04"
    )
    val creationDate: LocalDate? = null,

    @field:Schema(
        description = "day of sign loan request",
        name = "signDate",
        example = "2023-04-07"
    )
    val signDate: LocalDate? = null,

    @field:Schema(
        description = "ses code for client to sign documents",
        name = "sesCode",
        example = "2134"
    )
    val sesCode: Int? = null
)
