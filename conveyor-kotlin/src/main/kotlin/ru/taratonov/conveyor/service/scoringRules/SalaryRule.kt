package ru.taratonov.conveyor.service.scoringRules

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.taratonov.conveyor.dto.ScoringRuleDto
import ru.taratonov.conveyor.util.error
import java.math.BigDecimal

@Component
class SalaryRule(
    @Value("\${custom.text.nonNull}") private val NON_VALUE: String
) : Rule {
    override fun check(
        scoringRule: ScoringRuleDto,
        exceptions: MutableList<String>,
        personalRate: BigDecimal
    ): BigDecimal {
        if (
            (scoringRule.amount ?: error(NON_VALUE)) >=
            (scoringRule.salary?.multiply(BigDecimal.valueOf(20)) ?: error(NON_VALUE))
        ) {
            exceptions.add("The loan amount is more than 20 salaries")
        }
        return personalRate
    }
}