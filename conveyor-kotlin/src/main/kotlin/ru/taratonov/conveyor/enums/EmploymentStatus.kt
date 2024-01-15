package ru.taratonov.conveyor.enums

import com.fasterxml.jackson.annotation.JsonCreator
import lombok.AllArgsConstructor
import lombok.Getter
import ru.taratonov.conveyor.exception.IllegalArgumentOfEnumException

@AllArgsConstructor
@Getter
enum class EmploymentStatus(private val title: String) {
    UNEMPLOYED("unemployed"),
    SELF_EMPLOYED("self-employed"),
    BUSINESS_OWNER("business-owner"),
    EMPLOYED("employed");

    companion object {
        @JvmStatic
        @JsonCreator
        fun findValue(findValue: String): EmploymentStatus {
            return entries
                .find { it.name.lowercase() == findValue.lowercase() } ?: throw IllegalArgumentOfEnumException
                .createWith(
                    entries
                        .map(EmploymentStatus::title)
                        .toList()
                )
        }
    }

}