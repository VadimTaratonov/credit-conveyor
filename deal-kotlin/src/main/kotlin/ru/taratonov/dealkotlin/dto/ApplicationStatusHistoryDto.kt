package ru.taratonov.dealkotlin.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import io.swagger.v3.oas.annotations.media.Schema
import ru.taratonov.dealkotlin.enums.ApplicationStatus
import ru.taratonov.dealkotlin.enums.ChangeType
import java.time.LocalDate

data class ApplicationStatusHistoryDto(
    @field:Schema(
        description = "application status",
        name = "status",
        example = "PREAPPROVAL"
    )
    val status: ApplicationStatus? = null,

    @field:Schema(
        description = "time when status was changed",
        name = "time",
        example = "2023-10-12"
    )
    @field:JsonFormat(pattern = "yyyy-MM-dd")
    @field:JsonSerialize(using = LocalDateSerializer::class)
    @field:JsonDeserialize(using = LocalDateDeserializer::class)
    val time: LocalDate? = null,

    @field:Schema(
        description = "type of status change",
        name = "changeType",
        example = "AUTOMATIC"
    )
    val changeType: ChangeType? = null
)
