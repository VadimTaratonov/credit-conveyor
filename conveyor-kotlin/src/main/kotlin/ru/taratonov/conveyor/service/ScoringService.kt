package ru.taratonov.conveyor.service

import ru.taratonov.conveyor.dto.ScoringDataDTO
import java.math.BigDecimal

interface ScoringService {
    fun scoringPerson(scoringData: ScoringDataDTO): BigDecimal
    fun scoringPerson(isInsuranceEnabled: Boolean, isSalaryClient: Boolean): BigDecimal
}