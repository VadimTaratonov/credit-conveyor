package ru.taratonov.dealkotlin.service

import org.aspectj.lang.ProceedingJoinPoint
import ru.taratonov.dealkotlin.enums.AuditActionType
import ru.taratonov.dealkotlin.enums.Theme
import ru.taratonov.dealkotlin.model.Application

interface DocumentKafkaService {
    fun sendMessage(application: Application, theme: Theme)

    fun sendMessage(joinPoint: ProceedingJoinPoint, type: AuditActionType)

    fun createMessage(joinPoint: ProceedingJoinPoint): String

    fun sendDocuments(id: Long)

    fun requestSignDocument(id: Long)

    fun signDocument(id: Long, sesCode: Int)

    fun denyApplication(id: Long)
}