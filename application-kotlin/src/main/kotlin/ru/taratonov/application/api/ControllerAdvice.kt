package ru.taratonov.application.api

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.taratonov.application.dto.ErrorDto
import ru.taratonov.application.exception.IllegalDataFromOtherMsException
import java.time.LocalDateTime

@RestControllerAdvice
class ControllerAdvice {
    private val logger = KotlinLogging.logger { }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        MethodArgumentNotValidException::class
    )
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): List<ErrorDto> {
        logger.error("Handle MethodArgumentNotValidException", ex)
        val errors = mutableListOf<ErrorDto>()
        ex.bindingResult.fieldErrors.forEach {
            errors.add(ErrorDto("${it.field} ${it.defaultMessage}", LocalDateTime.now(), HttpStatus.BAD_REQUEST))
        }
        return errors
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalDataFromOtherMsException::class)
    fun handleOtherException(ex: Exception): ErrorDto {
        logger.error("Handle Exception", ex)
        return ErrorDto(ex.message ?: "error in Application API", LocalDateTime.now(), HttpStatus.BAD_REQUEST)
    }
}