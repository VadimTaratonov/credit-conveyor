package ru.taratonov.conveyor.api

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.taratonov.conveyor.dto.ErrorDTO
import ru.taratonov.conveyor.exception.IllegalArgumentOfEnumException
import ru.taratonov.conveyor.exception.ScoringException
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

@RestControllerAdvice
class ControllerAdvice {

    private val logger = KotlinLogging.logger { }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): List<ErrorDTO> {
        logger.error("Handle MethodArgumentNotValidException", ex)
        val errors = mutableListOf<ErrorDTO>()
        ex.bindingResult.fieldErrors
            .forEach {
                errors.add(
                    ErrorDTO(
                        it.field + " " + it.defaultMessage,
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST
                    )
                )
            }
        return errors
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        ScoringException::class,
        IllegalArgumentException::class
    )
    fun handleOtherException(ex: RuntimeException): ErrorDTO {
        logger.error("Handle Exception", ex)
        return ErrorDTO(ex.message ?: "error in Conveyor API", LocalDateTime.now(), HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        IllegalArgumentOfEnumException::class,
        DateTimeParseException::class
    )
    fun handleLongException(ex: RuntimeException): ErrorDTO {
        logger.error("Handle Exception", ex)
        return ErrorDTO(
            ex.cause?.cause?.message ?: "error in Conveyor API",
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST
        )
    }
}