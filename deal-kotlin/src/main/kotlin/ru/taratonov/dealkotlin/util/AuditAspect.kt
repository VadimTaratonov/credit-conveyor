package ru.taratonov.dealkotlin.util

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import ru.taratonov.dealkotlin.enums.AuditActionType
import ru.taratonov.dealkotlin.service.DocumentKafkaService

@Aspect
@Component
class AuditAspect(
    private val documentKafkaService: DocumentKafkaService
) {
    @Around("@annotation(ru.taratonov.dealkotlin.annotation.Audit)")
    fun auditActionToAuditMs(joinPoint: ProceedingJoinPoint): Any {
        documentKafkaService.sendMessage(joinPoint, AuditActionType.START)
        val proceed: Any?

        try {
            proceed = joinPoint.proceed()
        } catch (e: Throwable) {
            documentKafkaService.sendMessage(joinPoint, AuditActionType.FAILURE)
            throw RuntimeException(e)
        }

        documentKafkaService.sendMessage(joinPoint, AuditActionType.SUCCESS)
        return proceed
    }
}