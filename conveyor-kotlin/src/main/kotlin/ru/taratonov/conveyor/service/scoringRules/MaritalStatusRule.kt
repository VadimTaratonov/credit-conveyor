package ru.taratonov.conveyor.service.scoringRules

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.taratonov.conveyor.dto.ScoringRuleDto
import ru.taratonov.conveyor.enums.MaritalStatus
import ru.taratonov.conveyor.util.error
import java.math.BigDecimal

@Component
class MaritalStatusRule(
    @Value("\${custom.text.nonNull}") private val NON_VALUE: String
) : Rule {
    override fun check(
        scoringRule: ScoringRuleDto,
        exceptions: MutableList<String>,
        personalRate: BigDecimal
    ): BigDecimal {
        var rate = personalRate
        rate = when (scoringRule.maritalStatus ?: error(NON_VALUE)) {
            MaritalStatus.DIVORCED -> rate.add(BigDecimal.valueOf(1))
            MaritalStatus.MARRIED -> rate.subtract(BigDecimal.valueOf(3))
            MaritalStatus.SINGLE, MaritalStatus.WIDOW_WIDOWER -> rate
        }
        return rate
    }
}