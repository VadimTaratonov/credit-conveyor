package ru.taratonov.dealkotlin.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class PassportDto(
    @field:Schema(
        description = "passport series of person",
        name = "series",
        example = "2021"
    )
    val series: String? = null,

    @field:Schema(
        description = "passport number of person",
        name = "number",
        example = "111111"
    )
    val number: String? = null,

    @field:Schema(
        description = "passport issuing department",
        name = "issueBranch",
        example = "ГУ МВД РОССИИ"
    )
    var issueBranch: String? = null,

    @field:Schema(
        description = "date of issue of the passport",
        name = "issueDate",
        example = "2010-01-01"
    )
    @field:JsonFormat(pattern = "yyyy-MM-dd")
    @field:JsonSerialize(using = LocalDateSerializer::class)
    @field:JsonDeserialize(using = LocalDateDeserializer::class)
    var issueDate: LocalDate? = null
)
