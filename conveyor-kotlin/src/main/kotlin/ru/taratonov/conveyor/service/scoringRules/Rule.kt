package ru.taratonov.conveyor.service.scoringRules

import ru.taratonov.conveyor.dto.ScoringRuleDto
import java.math.BigDecimal

interface Rule {
    fun check(scoringRule: ScoringRuleDto, exceptions: MutableList<String>, personalRate: BigDecimal): BigDecimal
}