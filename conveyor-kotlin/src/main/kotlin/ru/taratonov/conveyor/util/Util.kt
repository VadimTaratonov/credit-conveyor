package ru.taratonov.conveyor.util

import ru.taratonov.conveyor.exception.ScoringException

fun error(message: String): Nothing {
    throw ScoringException.createWith(listOf(message))
}