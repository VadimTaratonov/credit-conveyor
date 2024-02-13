package ru.taratonov.conveyor.enums

import com.fasterxml.jackson.annotation.JsonCreator
import ru.taratonov.conveyor.exception.IllegalArgumentOfEnumException

enum class Gender(private val title: String) {
    MALE("male"),
    FEMALE("female"),
    NON_BINARY("non-binary");

    companion object {
        @JvmStatic
        @JsonCreator
        fun findValue(findValue: String): Gender {
            return entries
                .find { it.name.lowercase() == findValue.lowercase() } ?: throw IllegalArgumentOfEnumException
                .createWith(
                    entries
                        .map(Gender::title)
                        .toList()
                )
        }
    }
}