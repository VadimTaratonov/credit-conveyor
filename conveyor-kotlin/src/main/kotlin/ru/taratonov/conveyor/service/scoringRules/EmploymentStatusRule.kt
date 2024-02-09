package ru.taratonov.conveyor.service.scoringRules

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.taratonov.conveyor.dto.ScoringRuleDto
import ru.taratonov.conveyor.enums.EmploymentStatus
import ru.taratonov.conveyor.util.error
import java.math.BigDecimal

@Component
class EmploymentStatusRule(
    @Value("\${custom.text.nonNull}") private val NON_VALUE: String
) : Rule {
    override fun check(
        scoringRule: ScoringRuleDto,
        exceptions: MutableList<String>,
        personalRate: BigDecimal
    ): BigDecimal {
        var rate = personalRate
        when (scoringRule.employmentStatus ?: error(NON_VALUE)) {
            EmploymentStatus.UNEMPLOYED -> exceptions.add("Don't issue a loan to the unemployed")
            EmploymentStatus.SELF_EMPLOYED -> rate = rate.add(BigDecimal.valueOf(1))
            EmploymentStatus.BUSINESS_OWNER -> rate = rate.add(BigDecimal.valueOf(3))
            EmploymentStatus.EMPLOYED -> {}
        }
        return rate
    }


}