package ru.taratonov.dealkotlin.dto

import io.swagger.v3.oas.annotations.media.Schema
import ru.taratonov.dealkotlin.enums.Theme

data class EmailMessageDto(
    @field:Schema(
        description = "client's email",
        name = "emailAddress",
        example = "taratonovv8@bk.ru"
    )
    val emailAddress: String? = null,

    @field:Schema(
        description = "Theme of email message",
        name = "theme",
        example = "CREATE_DOCUMENTS"
    )
    val theme: Theme? = null,

    @field:Schema(
        description = "application id",
        name = "applicationId",
        example = "1"
    )
    val applicationId: Long? = null
)
