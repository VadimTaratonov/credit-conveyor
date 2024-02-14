package ru.taratonov.application.util

fun nullException(message: String): Nothing {
    throw NullPointerException(message)
}