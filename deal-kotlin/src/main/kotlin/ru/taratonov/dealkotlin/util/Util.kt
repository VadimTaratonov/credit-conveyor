package ru.taratonov.dealkotlin.util

import ru.taratonov.dealkotlin.exception.NotFoundException

fun notFound(message: String): Nothing {
    throw NotFoundException(message)
}

fun nullException(message: String): Nothing {
    throw NullPointerException(message)
}