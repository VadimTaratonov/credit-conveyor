package ru.taratonov.conveyor.service.scoringRules

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.taratonov.conveyor.dto.ScoringRuleDto
import ru.taratonov.conveyor.enums.Gender
import ru.taratonov.conveyor.util.error
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period

@Component
class GenderRule(
    @Value("\${custom.text.nonNull}") private val NON_VALUE: String
) : Rule {
    override fun calculate(
        scoringRule: ScoringRuleDto,
        exceptions: MutableList<String>,
        personalRate: BigDecimal
    ): BigDecimal {
        var rate = personalRate
        val age = Period.between(scoringRule.birthdate ?: error(NON_VALUE), LocalDate.now()).years
        when (scoringRule.gender ?: error(NON_VALUE)) {
            Gender.MALE -> {
                if (age in 30..55) {
                    rate = personalRate.subtract(BigDecimal.valueOf(3))
                }
            }

            Gender.FEMALE -> {
                if (age in 35..60) {
                    rate = personalRate.subtract(BigDecimal.valueOf(3))
                }
            }

            Gender.NON_BINARY -> rate = personalRate.add(BigDecimal.valueOf(3))
        }
        return rate
    }
}