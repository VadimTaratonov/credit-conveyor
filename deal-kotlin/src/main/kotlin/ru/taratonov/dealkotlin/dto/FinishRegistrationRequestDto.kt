package ru.taratonov.dealkotlin.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.format.annotation.DateTimeFormat
import ru.taratonov.dealkotlin.enums.Gender
import ru.taratonov.dealkotlin.enums.MaritalStatus
import java.time.LocalDate

data class FinishRegistrationRequestDto(
    @field:Schema(
        description = "person's gender",
        name = "gender",
        example = "MALE"
    )
    val gender: Gender? = null,

    @field:Schema(
        description = "person's marital status",
        name = "maritalStatus",
        example = "MARRIED"
    )
    val maritalStatus: MaritalStatus? = null,

    @field:Schema(
        description = "number of dependents",
        name = "dependentAmount",
        example = "1"
    )
    @field:Min(value = 0, message = "must be greater or equal than 0")
    val dependentAmount: Int? = null,

    @field:Schema(
        description = "date of issue of the passport",
        name = "passportIssueDate",
        example = "2010-01-01"
    )
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val passportIssueDate: LocalDate? = null,

    @field:Schema(
        description = "passport issuing department",
        name = "passportIssueBranch",
        example = "ГУ МВД РОССИИ"
    )
    val passportIssueBranch: String? = null,

    @field:Schema(
        description = "information about person at work",
        name = "employment"
    )
    val employment: @Valid EmploymentDto? = null,

    @field:Schema(
        description = "person's account",
        name = "account",
        example = "124353"
    )
    val account: String? = null
)
