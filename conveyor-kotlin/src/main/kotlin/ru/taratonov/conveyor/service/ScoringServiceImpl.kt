package ru.taratonov.conveyor.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.taratonov.conveyor.dto.ScoringDataDTO
import ru.taratonov.conveyor.enums.EmploymentStatus
import ru.taratonov.conveyor.enums.Gender
import ru.taratonov.conveyor.enums.MaritalStatus
import ru.taratonov.conveyor.enums.Position
import ru.taratonov.conveyor.exception.ScoringException
import ru.taratonov.conveyor.util.error
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period

@Service
class ScoringServiceImpl(
    @Value("\${base.rate}") private val BASE_RATE: BigDecimal,
    @Value("\${insurance.rate.reduction}") private val INSURANCE_RATE_REDUCTION: BigDecimal,
    @Value("\${salary.rate.reduction}") private val SALARY_RATE_REDUCTION: BigDecimal,
    @Value("\${text.nonNull}") private val NON_VALUE: String
) : ScoringService {
    private val logger = KotlinLogging.logger { }

    override fun scoringPerson(scoringData: ScoringDataDTO): BigDecimal {
        logger.debug("!START SCORING PERSON {} {}!", scoringData.firstName, scoringData.lastName)

        var personalRate = scoringPerson(
            scoringData.isInsuranceEnabled ?: error(NON_VALUE),
            scoringData.isSalaryClient ?: error(NON_VALUE)
        )
        val exceptions = mutableListOf<String>()
        val employment = scoringData.employment ?: error(NON_VALUE)

        // EmploymentStatus
        when (employment.employmentStatus ?: error(NON_VALUE)) {
            EmploymentStatus.UNEMPLOYED -> exceptions.add("Don't issue a loan to the unemployed")
            EmploymentStatus.SELF_EMPLOYED -> personalRate = personalRate.add(BigDecimal.valueOf(1))
            EmploymentStatus.BUSINESS_OWNER -> personalRate = personalRate.add(BigDecimal.valueOf(3))
            EmploymentStatus.EMPLOYED -> {}
        }

        // Position
        personalRate = when (employment.position ?: error(NON_VALUE)) {
            Position.MANAGER -> personalRate.subtract(BigDecimal.valueOf(2))
            Position.TOP_MANAGER -> personalRate.subtract(BigDecimal.valueOf(4))
            Position.MID_MANAGER, Position.WORKER, Position.OWNER -> personalRate
        }

        // MaritalStatus
        personalRate = when (scoringData.maritalStatus ?: error(NON_VALUE)) {
            MaritalStatus.DIVORCED -> personalRate.add(BigDecimal.valueOf(1))
            MaritalStatus.MARRIED -> personalRate.subtract(BigDecimal.valueOf(3))
            MaritalStatus.SINGLE, MaritalStatus.WIDOW_WIDOWER -> personalRate
        }


        // DependentAmount
        if ((scoringData.dependentAmount ?: error(NON_VALUE)) > 1) {
            personalRate = personalRate.add(BigDecimal.valueOf(1))
        }

        // Age
        val age = Period.between(scoringData.birthdate ?: error(NON_VALUE), LocalDate.now()).years
        if (age < 20) {
            exceptions.add("Age less than 20")
        }
        if (age > 60) {
            exceptions.add("Age more than 60")
        }

        // Gender
        when (scoringData.gender ?: error(NON_VALUE)) {
            Gender.MALE -> {
                if (age in 30..55) {
                    personalRate = personalRate.subtract(BigDecimal.valueOf(3))
                }
            }

            Gender.FEMALE -> {
                if (age in 35..60) {
                    personalRate = personalRate.subtract(BigDecimal.valueOf(3))
                }
            }

            Gender.NON_BINARY -> personalRate = personalRate.add(BigDecimal.valueOf(3))
        }

        // Salary
        if (
            (scoringData.amount ?: error(NON_VALUE)) >=
            (employment.salary?.multiply(BigDecimal.valueOf(20)) ?: error(NON_VALUE)
                    )
        ) {
            exceptions.add("The loan amount is more than 20 salaries")
        }

        // WorkExperience
        if ((employment.workExperienceTotal ?: error(NON_VALUE)) < 12) {
            exceptions.add("Total experience less than 12 months")
        }
        if ((employment.workExperienceCurrent ?: error(NON_VALUE)) < 3) {
            exceptions.add("Current experience less than 3 months")
        }

        logger.debug("!FINISH PERSON SCORING!")

        if (exceptions.isNotEmpty()) {
            throw ScoringException.createWith(exceptions)
        }

        if (personalRate <= BigDecimal.ZERO) {
            personalRate = BigDecimal.valueOf(0.1)
        }

        logger.info("Personal rate after scoring is {}", personalRate)
        return personalRate
    }

    override fun scoringPerson(isInsuranceEnabled: Boolean, isSalaryClient: Boolean): BigDecimal {
        logger.debug("!BASE PERSON SCORING!")
        var personalRate = BASE_RATE
        logger.debug("BASE RATE IN OUR BANK IS {}", BASE_RATE)

        personalRate = if (isInsuranceEnabled) personalRate.subtract(INSURANCE_RATE_REDUCTION) else personalRate
        personalRate = if (isSalaryClient) personalRate.subtract(SALARY_RATE_REDUCTION) else personalRate
        personalRate = if (personalRate <= BigDecimal.ZERO) BigDecimal.valueOf(0.1) else personalRate
        logger.debug("!FINISH BASE SCORING. Base personal rate is ready - {}", personalRate)

        return personalRate
    }
}