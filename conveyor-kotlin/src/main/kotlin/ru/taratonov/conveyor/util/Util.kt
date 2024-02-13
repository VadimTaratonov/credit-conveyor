package ru.taratonov.conveyor.util

import ru.taratonov.conveyor.dto.ScoringDataDTO
import ru.taratonov.conveyor.dto.ScoringRuleDto
import ru.taratonov.conveyor.exception.ScoringException

fun error(message: String): Nothing {
    throw ScoringException.createWith(listOf(message))
}

fun ScoringDataDTO.mapToScoringRule(): ScoringRuleDto {
    val nonNull = "Should not be null"
    return ScoringRuleDto(
        employmentStatus = employment?.employmentStatus ?: error(nonNull),
        position = employment.position ?: error(nonNull),
        maritalStatus = maritalStatus ?: error(nonNull),
        dependentAmount = dependentAmount ?: error(nonNull),
        birthdate = birthdate ?: error(nonNull),
        gender = gender ?: error(nonNull),
        amount = amount ?: error(nonNull),
        salary = employment.salary ?: error(nonNull),
        workExperienceCurrent = employment.workExperienceCurrent ?: error(nonNull),
        workExperienceTotal = employment.workExperienceTotal ?: error(nonNull),
    )
}