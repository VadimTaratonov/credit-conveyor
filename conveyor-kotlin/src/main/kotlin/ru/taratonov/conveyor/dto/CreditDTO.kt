package ru.taratonov.conveyor.dto

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Builder
import lombok.Data
import java.math.BigDecimal

@Builder
@Data
data class CreditDTO(
    @field:Schema(
        description = "amount of the loan",
        name = "amount",
        example = "10000"
    )
    val amount: BigDecimal,

    @field:Schema(
        description = "loan term",
        name = "term",
        example = "7"
    )
    val term: Int,

    @field:Schema(
        description = "monthly payment of the loan",
        name = "monthlyPayment",
        example = "1245.67"
    )
    val monthlyPayment: BigDecimal,

    @field:Schema(
        description = "loan rate",
        name = "rate",
        example = "3"
    )
    val rate: BigDecimal,

    @field:Schema(
        description = "full cost of the loan",
        name = "psk",
        example = "2.56"
    )
    val psk: BigDecimal,

    @field:Schema(
        description = "availability of credit insurance",
        name = "isInsuranceEnabled",
        example = "false"
    )
    val isInsuranceEnabled: Boolean,

    @field:Schema(
        description = "salary client",
        name = "isSalaryClient",
        example = "true"
    )
    val isSalaryClient: Boolean,

    @field:Schema(
        description = "list of payments",
        name = "paymentSchedule"
    )
    val paymentSchedule: List<PaymentScheduleElement>
)
