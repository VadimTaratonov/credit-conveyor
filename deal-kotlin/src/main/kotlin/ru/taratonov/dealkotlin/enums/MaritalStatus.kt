package ru.taratonov.dealkotlin.enums

import com.fasterxml.jackson.annotation.JsonCreator
import ru.taratonov.dealkotlin.exception.IllegalArgumentOfEnumException

enum class MaritalStatus(private val title: String) {
    DIVORCED("divorced"),
    MARRIED("married"),
    SINGLE("single"),
    WIDOW_WIDOWER("widow-widower");

    companion object {
        @JvmStatic
        @JsonCreator
        fun findValue(findValue: String): MaritalStatus {
            return entries
                .find { it.name.lowercase() == findValue.lowercase() } ?: throw IllegalArgumentOfEnumException
                .createWith(
                    entries
                        .map(MaritalStatus::title)
                        .toList()
                )
        }
    }
}