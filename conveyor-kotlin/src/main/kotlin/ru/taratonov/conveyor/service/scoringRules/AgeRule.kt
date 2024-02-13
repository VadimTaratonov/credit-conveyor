package ru.taratonov.conveyor.service.scoringRules

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.taratonov.conveyor.dto.ScoringRuleDto
import ru.taratonov.conveyor.util.error
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period

@Component
class AgeRule(
    @Value("\${custom.text.nonNull}") private val NON_VALUE: String
) : Rule {
    override fun calculate(
        scoringRule: ScoringRuleDto,
        exceptions: MutableList<String>,
        personalRate: BigDecimal
    ): BigDecimal {
        val age = Period.between(scoringRule.birthdate ?: error(NON_VALUE), LocalDate.now()).years
        if (age < 20) {
            exceptions.add("Age less than 20")
        }
        if (age > 60) {
            exceptions.add("Age more than 60")
        }
        return personalRate
    }
}