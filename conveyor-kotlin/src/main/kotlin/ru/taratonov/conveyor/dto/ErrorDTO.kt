package ru.taratonov.conveyor.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ErrorDTO(
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
    val errorTime: LocalDateTime? = null,

    @field:Schema(
        description = "http status of error",
        name = "httpStatus",
        example = "BAD_REQUEST"
    )
    val httpStatus: HttpStatus? = null
)
