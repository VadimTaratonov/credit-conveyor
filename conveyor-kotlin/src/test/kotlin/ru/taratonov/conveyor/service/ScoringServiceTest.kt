package ru.taratonov.conveyor.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.taratonov.conveyor.dto.EmploymentDTO
import ru.taratonov.conveyor.dto.ScoringDataDTO
import ru.taratonov.conveyor.enums.EmploymentStatus
import ru.taratonov.conveyor.enums.Gender
import ru.taratonov.conveyor.enums.MaritalStatus
import ru.taratonov.conveyor.enums.Position
import ru.taratonov.conveyor.exception.ScoringException
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest
class ScoringServiceTest {

    @Autowired
    private lateinit var scoringService: ScoringService

    private val baseRate = BigDecimal.valueOf(8)

    private val scoringDataWithException = ScoringDataDTO(
        amount = BigDecimal.valueOf(30),
        gender = Gender.FEMALE,
        employment = EmploymentDTO(
            employmentStatus = EmploymentStatus.UNEMPLOYED,
            position = Position.MANAGER,
            salary = BigDecimal.ONE,
            workExperienceCurrent = 2,
            workExperienceTotal = 2
        ),
        maritalStatus = MaritalStatus.DIVORCED,
        dependentAmount = 2,
        birthdate = LocalDate.of(2021, 10, 2),
        isInsuranceEnabled = false,
        isSalaryClient = false
    )

    private val scoringDataWithoutException = ScoringDataDTO(
        employment = EmploymentDTO(
            employmentStatus = EmploymentStatus.SELF_EMPLOYED,
            position = Position.MANAGER,
            salary = BigDecimal.ONE,
            workExperienceCurrent = 5,
            workExperienceTotal = 14
        ),
        amount = BigDecimal.valueOf(10),
        maritalStatus = MaritalStatus.DIVORCED,
        dependentAmount = 2,
        birthdate = LocalDate.of(1980, 10, 2),
        gender = Gender.MALE,
        isInsuranceEnabled = false,
        isSalaryClient = false
    )

    @Test
    fun `test scoring method with data that is valid`() {
        val actual = scoringService.scoringPerson(scoringDataWithoutException)
        val expected = BigDecimal.valueOf(6)

        assertEquals(expected, actual)
    }

    @Test
    fun `test scoring method with data that is not valid`() {
        val scoringException =
            assertThrows(ScoringException::class.java) { scoringService.scoringPerson(scoringDataWithException) }
        val expectedMessage = "Don't issue a loan to the unemployed, " +
                "Age less than 20, " +
                "The loan amount is more than 20 salaries, " +
                "Total experience less than 12 months, " +
                "Current experience less than 3 months"

        assertEquals(expectedMessage, scoringException.message)
    }

    @Test
    fun `test scoring method with data contains null and throw exception`() {
        val scoringDataWithNulls = ScoringDataDTO()
        val scoringException =
            assertThrows(ScoringException::class.java) { scoringService.scoringPerson(scoringDataWithNulls) }
        val expectedMessage = "Should not be null"

        assertEquals(expectedMessage, scoringException.message)
    }

    @Test
    fun `test basic method of scoring person who is salary client and has insurance`() {
        val isInsuranceEnabled = true
        val isSalaryClient = true

        val actual = scoringService.scoringPerson(isInsuranceEnabled, isSalaryClient)
        val expected = baseRate.subtract(BigDecimal.valueOf(4))

        assertEquals(expected, actual)
    }

    @Test
    fun `test basic method of scoring person who is not salary client and has insurance`() {
        val isInsuranceEnabled = true
        val isSalaryClient = false

        val actual = scoringService.scoringPerson(isInsuranceEnabled, isSalaryClient)
        val expected = baseRate.subtract(BigDecimal.valueOf(3))

        assertEquals(expected, actual)
    }

    @Test
    fun `test basic method of scoring person who is salary client and has not insurance`() {
        val isInsuranceEnabled = false
        val isSalaryClient = true

        val actual = scoringService.scoringPerson(isInsuranceEnabled, isSalaryClient)
        val expected = baseRate.subtract(BigDecimal.ONE)

        assertEquals(expected, actual)
    }

    @Test
    fun `test basic method of scoring person who is not salary client and has not insurance`() {
        val isInsuranceEnabled = false
        val isSalaryClient = false

        val actual = scoringService.scoringPerson(isInsuranceEnabled, isSalaryClient)
        val expected = baseRate

        assertEquals(expected, actual)
    }
}