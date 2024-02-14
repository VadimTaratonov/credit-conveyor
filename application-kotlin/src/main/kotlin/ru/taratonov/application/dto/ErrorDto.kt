package ru.taratonov.application.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ErrorDto(
    @field:Schema(
        description = "error message",
        name = "msg",
        example = "term must be greater or equal than 6"
    )
    val msg: String? = null,

    @field:Schema(
        description = "time of error",
        name = "errorTime",
        example = "2023-08-10T12:31:43.1545756"
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS")
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    @JsonDeserialize(using = LocalDateTimeDeserializer::class)
    val errorTime: LocalDateTime? = null,

    @field:Schema(
        description = "http status of error",
        name = "httpStatus",
        example = "BAD_REQUEST"
    )
    val httpStatus: HttpStatus? = null
)
