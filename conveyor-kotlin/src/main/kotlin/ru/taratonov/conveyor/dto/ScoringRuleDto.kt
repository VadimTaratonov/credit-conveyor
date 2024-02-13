package ru.taratonov.conveyor.dto

import io.swagger.v3.oas.annotations.media.Schema
import ru.taratonov.conveyor.enums.EmploymentStatus
import ru.taratonov.conveyor.enums.Gender
import ru.taratonov.conveyor.enums.MaritalStatus
import ru.taratonov.conveyor.enums.Position
import java.math.BigDecimal
import java.time.LocalDate

data class ScoringRuleDto(
    @field:Schema(
        description = "person's working status",
        name = "employmentStatus",
        example = "SELF_EMPLOYED"
    )
    val employmentStatus: EmploymentStatus? = null,

    @field:Schema(
        description = "the user's position on the job",
        name = "position",
        example = "MANAGER"
    )
    val position: Position? = null,

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
    val dependentAmount: Int? = null,

    @field:Schema(
        description = "birthday of person",
        name = "birthdate",
        example = "2001-10-02"
    )
    val birthdate: LocalDate? = null,

    @field:Schema(
        description = "person's gender",
        name = "gender",
        example = "MALE"
    )
    val gender: Gender? = null,

    @field:Schema(
        description = "the amount requested by the client",
        name = "amount",
        example = "10000"
    )
    val amount: BigDecimal? = null,

    @field:Schema(
        description = "persons salary",
        name = "salary",
        example = "12000"
    )
    val salary: BigDecimal? = null,

    @field:Schema(
        description = "total work experience of person",
        name = "workExperienceTotal",
        example = "12"
    )
    val workExperienceTotal: Int? = null,

    @field:Schema(
        description = "current work experience of person in job",
        name = "workExperienceCurrent",
        example = "5"
    )
    val workExperienceCurrent: Int? = null
)
