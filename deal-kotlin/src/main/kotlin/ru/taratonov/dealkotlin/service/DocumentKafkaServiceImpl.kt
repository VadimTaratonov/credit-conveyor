package ru.taratonov.dealkotlin.service

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.taratonov.dealkotlin.dto.AuditAction
import ru.taratonov.dealkotlin.dto.EmailMessageDto
import ru.taratonov.dealkotlin.enums.ApplicationStatus
import ru.taratonov.dealkotlin.enums.AuditActionServiceType
import ru.taratonov.dealkotlin.enums.AuditActionType
import ru.taratonov.dealkotlin.enums.CreditStatus
import ru.taratonov.dealkotlin.enums.Theme
import ru.taratonov.dealkotlin.model.Application
import ru.taratonov.dealkotlin.repository.ApplicationRepository
import ru.taratonov.dealkotlin.repository.CreditRepository
import ru.taratonov.dealkotlin.util.notFound
import ru.taratonov.dealkotlin.util.nullException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Random

@Service
class DocumentKafkaServiceImpl(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper,
    private val applicationRepository: ApplicationRepository,
    private val creditRepository: CreditRepository,
    private val fillingDataService: FillingDataService,
    @Value("\${text.nonNull}") private val NOT_NULL: String
) : DocumentKafkaService {
    private val logger = KotlinLogging.logger { }

    override fun sendMessage(application: Application, theme: Theme) {
        val emailMessage = EmailMessageDto(
            emailAddress = application.client?.email ?: nullException(NOT_NULL),
            theme = theme,
            applicationId = application.applicationId ?: nullException(NOT_NULL)
        )
        logger.debug("EmailMessageDTO is ready to sending {}", emailMessage)
        emailMessage.theme?.title?.let { sendMessageToKafka(emailMessage, it) }
    }

    override fun sendMessage(joinPoint: ProceedingJoinPoint, type: AuditActionType) {
        val auditAction = AuditAction(
            type = type,
            serviceType = AuditActionServiceType.DEAL,
            message = createMessage(joinPoint)
        )
        sendMessageToKafka(auditAction, Theme.AUDIT.title)
    }

    override fun createMessage(joinPoint: ProceedingJoinPoint): String {
        return "Time ${LocalDateTime.now()} Method ${joinPoint.signature.name} with parameters ${joinPoint.args.map { it.toString() }}}"
    }

    override fun sendDocuments(id: Long) {
        var application = getApplication(id)
        application =
            fillingDataService.updateApplicationWithNewStatus(application, ApplicationStatus.PREPARE_DOCUMENTS)
        application = applicationRepository.save(application)
        sendMessage(application, Theme.SEND_DOCUMENTS)
        application =
            fillingDataService.updateApplicationWithNewStatus(application, ApplicationStatus.DOCUMENT_CREATED)
        applicationRepository.save(application)
    }

    override fun requestSignDocument(id: Long) {
        var application = getApplication(id)
        val sesCode = generateSesCode()
        application.sesCode = sesCode
        application = applicationRepository.save(application)
        sendMessage(application, Theme.SEND_SES)
    }

    override fun signDocument(id: Long, sesCode: Int) {
        var application = getApplication(id)
        val realCode = application.sesCode ?: nullException(NOT_NULL)
        if (sesCode == realCode) {
            application =
                fillingDataService.updateApplicationWithNewStatus(application, ApplicationStatus.DOCUMENT_SIGNED)
            application = applicationRepository.save(application)
            sendMessage(application, Theme.CREDIT_ISSUED)
            application =
                fillingDataService.updateApplicationWithNewStatus(application, ApplicationStatus.CREDIT_ISSUED)
            application.signDate = LocalDate.now()
            val credit = application.credit ?: nullException(NOT_NULL)
            credit.creditStatus = CreditStatus.ISSUED
            logger.info("status of credit was change to issued")
            creditRepository.save(credit)
            applicationRepository.save(application)
        } else {
            fillingDataService.updateApplicationWithNewStatus(application, ApplicationStatus.CLIENT_DENIED)
            application = applicationRepository.save(application)
            sendMessage(application, Theme.APPLICATION_DENIED)
        }
    }

    override fun denyApplication(id: Long) {
        var application = getApplication(id)
        fillingDataService.updateApplicationWithNewStatus(application, ApplicationStatus.CLIENT_DENIED)
        application = applicationRepository.save(application)
        sendMessage(application, Theme.APPLICATION_DENIED)
    }

    private fun sendMessageToKafka(any: Any, topic: String) {
        val message = objectMapper.writeValueAsString(any)
        kafkaTemplate.send(topic, message)
        logger.info("message send to kafka with data {} to topic {}", message, topic)
    }

    private fun getApplication(id: Long): Application {
        val optionalApplication = applicationRepository.findById(id)
        if (optionalApplication.isEmpty) {
            notFound("Application with id $id not found")
        }
        val application = optionalApplication.get()
        logger.info("application with id= {} received", application.applicationId)
        return application
    }

    private fun generateSesCode(): Int {
        val random = Random()
        val first = random.nextInt(9) + 1
        val second = random.nextInt(9) + 1
        val third = random.nextInt(9) + 1
        val fourth = random.nextInt(9) + 1
        val sesCode = first * 1000 + second * 100 + third * 10 + fourth
        logger.info("ses code: {} created", sesCode)
        return sesCode
    }
}