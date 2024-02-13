package ru.taratonov.conveyor.service.scoringRules

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.taratonov.conveyor.dto.ScoringRuleDto
import ru.taratonov.conveyor.util.error
import java.math.BigDecimal

@Component
class DependentAmountRule(
    @Value("\${custom.text.nonNull}") private val NON_VALUE: String
) : Rule {
    override fun calculate(
        scoringRule: ScoringRuleDto,
        exceptions: MutableList<String>,
        personalRate: BigDecimal
    ): BigDecimal {
        var rate = personalRate
        if ((scoringRule.dependentAmount ?: error(NON_VALUE)) > 1) {
            rate = personalRate.add(BigDecimal.valueOf(1))
        }
        return rate
    }
}