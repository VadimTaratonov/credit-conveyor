package ru.taratonov.conveyor.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate


data class LoanApplicationRequestDTO(
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
        example = "4"
    )
    @field:Min(value = 6, message = "must be greater or equal than 6")
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
        description = "email of person",
        name = "email",
        example = "taratonovv8@bk.ru"
    )
    @field:Pattern(regexp = "[\\w\\.]{2,50}@[\\w\\.]{2,20}", message = "doesn't match the right format")
    val email: String? = null,

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
    val passportNumber: String? = null
) {
    @AssertTrue(message = "must be no later than 18 years from the current day")
    fun isBirthDateValid(): Boolean {
        return !(birthdate?.plusYears(18)?.isAfter(LocalDate.now()) ?: true)
    }
}
