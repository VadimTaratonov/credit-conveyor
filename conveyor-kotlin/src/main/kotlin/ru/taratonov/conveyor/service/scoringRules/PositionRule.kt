package ru.taratonov.conveyor.service.scoringRules

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.taratonov.conveyor.dto.ScoringRuleDto
import ru.taratonov.conveyor.enums.Position
import ru.taratonov.conveyor.util.error
import java.math.BigDecimal

@Component
class PositionRule(
    @Value("\${custom.text.nonNull}") private val NON_VALUE: String
) : Rule {
    override fun calculate(
        scoringRule: ScoringRuleDto,
        exceptions: MutableList<String>,
        personalRate: BigDecimal
    ): BigDecimal {
        var rate = personalRate
        rate = when (scoringRule.position ?: error(NON_VALUE)) {
            Position.MANAGER -> rate.subtract(BigDecimal.valueOf(2))
            Position.TOP_MANAGER -> rate.subtract(BigDecimal.valueOf(4))
            Position.MID_MANAGER, Position.WORKER, Position.OWNER -> rate
        }
        return rate
    }
}