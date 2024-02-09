package ru.taratonov.conveyor.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.taratonov.conveyor.dto.ScoringDataDTO
import ru.taratonov.conveyor.exception.ScoringException
import ru.taratonov.conveyor.service.scoringRules.Rule
import ru.taratonov.conveyor.util.error
import ru.taratonov.conveyor.util.mapToScoringRule
import java.math.BigDecimal

@Service
class ScoringServiceImpl(
    @Value("\${custom.base.rate}") private val BASE_RATE: BigDecimal,
    @Value("\${custom.insurance.rate.reduction}") private val INSURANCE_RATE_REDUCTION: BigDecimal,
    @Value("\${custom.salary.rate.reduction}") private val SALARY_RATE_REDUCTION: BigDecimal,
    @Value("\${custom.text.nonNull}") private val NON_VALUE: String,
    private val rules: List<Rule>
) : ScoringService {
    private val logger = KotlinLogging.logger { }

    override fun scoringPerson(scoringData: ScoringDataDTO): BigDecimal {
        logger.debug("!START SCORING PERSON {} {}!", scoringData.firstName, scoringData.lastName)

        var personalRate = scoringPerson(
            scoringData.isInsuranceEnabled ?: error(NON_VALUE),
            scoringData.isSalaryClient ?: error(NON_VALUE)
        )
        val exceptions = mutableListOf<String>()

        val scoringRule = scoringData.mapToScoringRule()

        for (rule in rules) {
            personalRate = rule.check(scoringRule, exceptions, personalRate)
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