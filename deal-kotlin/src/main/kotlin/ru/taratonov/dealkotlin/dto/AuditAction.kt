package ru.taratonov.dealkotlin.dto

import io.swagger.v3.oas.annotations.media.Schema
import ru.taratonov.dealkotlin.enums.AuditActionServiceType
import ru.taratonov.dealkotlin.enums.AuditActionType

data class AuditAction(
    @field:Schema(
        description = "audit action type",
        name = "type",
        example = "START"
    )
    val type: AuditActionType? = null,

    @field:Schema(
        description = "audit action service type",
        name = "serviceType",
        example = "DEAL"
    )
    val serviceType: AuditActionServiceType? = null,

    @field:Schema(
        description = "message to add to audit",
        name = "message",
        example = "message"
    )
    val message: String? = null
)
