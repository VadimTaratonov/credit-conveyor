package ru.taratonov.conveyor.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
import lombok.Builder
import lombok.Data
import ru.taratonov.conveyor.enums.EmploymentStatus
import ru.taratonov.conveyor.enums.Position
import java.math.BigDecimal

@Builder
@Data
data class EmploymentDTO(
    @field:Schema(
        description = "person's working status",
        name = "employmentStatus",
        example = "SELF_EMPLOYED"
    )
     val employmentStatus: EmploymentStatus,

    @field:Schema(
        description = "persons inn",
        name = "employerINN",
        example = "1234567890"
    )
    @field:Pattern(
        regexp = "^(\\d{10}|\\d{12})$",
        message = "must include only 12 numbers for individuals and 10 to legal entities"
    )
     val employerINN: String,

    @field:Schema(
        description = "persons salary",
        name = "salary",
        example = "12000"
    )
    @field:Min(value = 0, message = "can't be less than 0")
     val salary: BigDecimal,

    @field:Schema(
        description = "the user's position on the job",
        name = "position",
        example = "MANAGER"
    )
     val position: Position,

    @field:Schema(
        description = "total work experience of person",
        name = "workExperienceTotal",
        example = "12"
    )
    @field:Min(value = 0)
     val workExperienceTotal: Int,

    @field:Schema(
        description = "current work experience of person in job",
        name = "workExperienceCurrent",
        example = "5"
    )
    @field:Min(value = 0)
     val workExperienceCurrent: Int
)
