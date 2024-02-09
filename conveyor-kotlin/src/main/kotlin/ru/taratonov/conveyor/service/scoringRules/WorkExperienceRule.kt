package ru.taratonov.conveyor.service.scoringRules

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.taratonov.conveyor.dto.ScoringRuleDto
import ru.taratonov.conveyor.util.error
import java.math.BigDecimal

@Component
class WorkExperienceRule(
    @Value("\${custom.text.nonNull}") private val NON_VALUE: String
) : Rule {
    override fun check(
        scoringRule: ScoringRuleDto,
        exceptions: MutableList<String>,
        personalRate: BigDecimal
    ): BigDecimal {
        if ((scoringRule.workExperienceTotal ?: error(NON_VALUE)) < 12) {
            exceptions.add("Total experience less than 12 months")
        }
        if ((scoringRule.workExperienceCurrent ?: error(NON_VALUE)) < 3) {
            exceptions.add("Current experience less than 3 months")
        }
        return personalRate
    }
}