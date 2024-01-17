package ru.taratonov.conveyor.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import lombok.Builder
import lombok.Data
import org.springframework.format.annotation.DateTimeFormat
import ru.taratonov.conveyor.enums.Gender
import ru.taratonov.conveyor.enums.MaritalStatus
import java.math.BigDecimal
import java.time.LocalDate

@Builder
@Data
data class ScoringDataDTO(
    @field:Schema(
        description = "the amount requested by the client",
        name = "amount",
        example = "10000"
    )
    @field:DecimalMin(value = "10000.0", message = "must be greater or equal than 10000")
    val amount: BigDecimal? = null,

    @field:Schema(
        description = "loan term",
        name = "term",
        example = "7"
    )
    @field:Min(value = 6, message = "can't take loan less than 6 month")
    val term: Int? = null,

    @field:Schema(
        description = "first name of person",
        name = "firstName",
        example = "Vadim"
    )
    @field:Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "must include only letters")
    @field:Size(min = 2, max = 30, message = "must be in range from 2 to 30 symbols")
    val firstName: String? = null,

    @field:Schema(
        description = "last name of person",
        name = "lastName",
        example = "Taratonov"
    )
    @field:Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "must include only letters")
    @field:Size(min = 2, max = 30, message = "must be in range from 2 to 30 symbols")
    val lastName: String? = null,

    @field:Schema(
        description = "middle name of person",
        name = "middleName",
        example = "Nikolaevich"
    )
    @field:Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "must include only letters")
    @field:Size(min = 2, max = 30, message = "must be in range from 2 to 30 symbols")
    val middleName: String? = null,

    @field:Schema(
        description = "person's gender",
        name = "gender",
        example = "MALE"
    )
    val gender: Gender? = null,

    @field:Schema(
        description = "birthday of person",
        name = "birthdate",
        example = "2001-10-02"
    )
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val birthdate: LocalDate? = null,

    @field:Schema(
        description = "passport series of person",
        name = "passportSeries",
        example = "2021"
    )
    @field:Pattern(regexp = "\\d+", message = "must include only numbers")
    @field:Size(min = 4, max = 4, message = "must be 4 digits long")
    val passportSeries: String? = null,

    @field:Schema(
        description = "passport number of person",
        name = "passportNumber",
        example = "111111"
    )
    @field:Pattern(regexp = "\\d+", message = "must include only numbers")
    @field:Size(min = 6, max = 6, message = "must be 6 digits long")
    val passportNumber: String? = null,

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
        description = "information about person at work",
        name = "employment"
    )
    @field:Valid
    val employment: EmploymentDTO? = null,

    @field:Schema(
        description = "person's account",
        name = "account",
        example = "124353"
    )
    val account: String? = null,

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
    @field:NotNull(message = "must not be empty")
    val isSalaryClient: Boolean? = null
)
