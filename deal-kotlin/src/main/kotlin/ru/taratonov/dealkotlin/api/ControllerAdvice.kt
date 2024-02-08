package ru.taratonov.dealkotlin.api


import jakarta.persistence.EntityNotFoundException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.taratonov.dealkotlin.dto.ErrorDto
import ru.taratonov.dealkotlin.exception.DatabaseException
import ru.taratonov.dealkotlin.exception.IllegalArgumentOfEnumException
import ru.taratonov.dealkotlin.exception.IllegalDataFromOtherMsException
import ru.taratonov.dealkotlin.exception.NotFoundException
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

@RestControllerAdvice
class ControllerAdvice {
    private val logger = KotlinLogging.logger { }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): List<ErrorDto> {
        logger.error("Handle MethodArgumentNotValidException", ex)
        val errors = mutableListOf<ErrorDto>()
        ex.bindingResult.fieldErrors.forEach {
            errors.add(ErrorDto("${it.field} ${it.defaultMessage}", LocalDateTime.now(), HttpStatus.BAD_REQUEST))
        }
        return errors
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        EntityNotFoundException::class,
        IllegalArgumentException::class,
        NullPointerException::class,
        IllegalDataFromOtherMsException::class,
        DatabaseException::class,
        IllegalArgumentOfEnumException::class,
        DateTimeParseException::class
    )
    fun handleOtherException(ex: Exception): ErrorDto {
        logger.error("Handle exception", ex)
        return ErrorDto(ex.message, LocalDateTime.now(), HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ErrorDto {
        logger.error("Handle not found xception", ex)
        return ErrorDto(ex.message, LocalDateTime.now(), HttpStatus.NOT_FOUND)
    }
}