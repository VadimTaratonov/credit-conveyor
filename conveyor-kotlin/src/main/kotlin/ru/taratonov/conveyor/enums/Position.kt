package ru.taratonov.conveyor.enums

import com.fasterxml.jackson.annotation.JsonCreator
import lombok.AllArgsConstructor
import lombok.Getter
import ru.taratonov.conveyor.exception.IllegalArgumentOfEnumException

@AllArgsConstructor
@Getter
enum class Position(private val title: String) {
    WORKER("worker"),
    MANAGER("manager"),
    TOP_MANAGER("top-manager"),
    MID_MANAGER("mid-manager"),
    OWNER("owner");

    companion object {
        @JvmStatic
        @JsonCreator
        fun findValue(findValue: String): Position {
            return entries
                .find { it.name.lowercase() == findValue.lowercase() } ?: throw IllegalArgumentOfEnumException
                .createWith(
                    entries
                        .map(Position::title)
                        .toList()
                )
        }
    }
}