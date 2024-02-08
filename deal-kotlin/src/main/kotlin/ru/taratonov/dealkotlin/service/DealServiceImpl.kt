package ru.taratonov.dealkotlin.service

import com.google.common.base.Throwables
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.taratonov.dealkotlin.annotation.Audit
import ru.taratonov.dealkotlin.dto.ApplicationDto
import ru.taratonov.dealkotlin.dto.CreditDto
import ru.taratonov.dealkotlin.dto.FinishRegistrationRequestDto
import ru.taratonov.dealkotlin.dto.LoanApplicationRequestDto
import ru.taratonov.dealkotlin.dto.LoanOfferDto
import ru.taratonov.dealkotlin.enums.ApplicationStatus
import ru.taratonov.dealkotlin.enums.Theme
import ru.taratonov.dealkotlin.exception.DatabaseException
import ru.taratonov.dealkotlin.exception.IllegalDataFromOtherMsException
import ru.taratonov.dealkotlin.repository.ApplicationRepository
import ru.taratonov.dealkotlin.repository.ClientRepository
import ru.taratonov.dealkotlin.repository.CreditRepository
import ru.taratonov.dealkotlin.util.notFound
import ru.taratonov.dealkotlin.util.nullException
import java.sql.SQLException

@Service
class DealServiceImpl(
    private val clientRepository: ClientRepository,
    private val applicationRepository: ApplicationRepository,
    private val creditRepository: CreditRepository,
    private val restTemplateRequestsService: RestTemplateRequestsService,
    private val fillingDataServiceImpl: FillingDataServiceImpl,
    private val documentKafkaService: DocumentKafkaService,
    @Value("\${text.nonNull}") private val NOT_NULL: String
) : DealService {
    private val logger = KotlinLogging.logger { }

    @Audit
    override fun getOffers(loanApplicationRequest: LoanApplicationRequestDto): List<LoanOfferDto> {
        logger.info(
            "Get loanApplicationRequestDTO and create new client with name - {}, surname - {}",
            loanApplicationRequest.firstName, loanApplicationRequest.lastName
        )

        val client = fillingDataServiceImpl.createClientOfRequest(loanApplicationRequest)

        try {
            clientRepository.save(client)
        } catch (e: Exception) {
            val rootCause = Throwables.getRootCause(e)
            if (rootCause is SQLException) {
                if ("23505" == rootCause.sqlState) {
                    throw DatabaseException(rootCause.message ?: "")
                }
            }
        }

        logger.debug("client {} {} is saved", client.firstName, client.lastName)

        var application = fillingDataServiceImpl.createApplicationOfRequest(client)
        application = applicationRepository.save(application)
        val applicationId = application.applicationId
        logger.debug("application with id= {} is saved", applicationId)

        val list = restTemplateRequestsService.requestToGetOffers(loanApplicationRequest)

        list.forEach { it.applicationId = applicationId }
        logger.debug("application id assigned to each loanOffer - {}", applicationId)
        return list
    }

    @Audit
    override fun chooseOffer(loanOffer: LoanOfferDto) {
        val applicationId = loanOffer.applicationId ?: nullException(NOT_NULL)
        val foundApplication = applicationRepository.findById(applicationId)
        if (foundApplication.isEmpty) {
            notFound("Application with id $applicationId not found")
        }
        var application = foundApplication.get()
        logger.info("application with id= {} received", application.applicationId)
        application = fillingDataServiceImpl.updateApplicationWhenChooseOffer(application, loanOffer)
        applicationRepository.save(application)
        logger.debug("application with id= {} is updated", application.applicationId)

        documentKafkaService.sendMessage(application, Theme.FINISH_REGISTRATION)
    }

    @Audit
    override fun calculateCredit(finishRegistrationRequest: FinishRegistrationRequestDto, id: Long) {
        val foundApplication = applicationRepository.findById(id)
        if (foundApplication.isEmpty) {
            notFound("Application with id $id not found")
        }
        var application = foundApplication.get()
        logger.info("application with id= {} received", application.applicationId)
        var client = application.client ?: nullException(NOT_NULL)

        val scoringDataDTO = fillingDataServiceImpl
            .fillAllInformationToScoringData(finishRegistrationRequest, client, application)
        logger.debug(
            "scoringDataDTO for {} {} is ready for calculating",
            scoringDataDTO.firstName, scoringDataDTO.lastName
        )

        client = fillingDataServiceImpl.fillAllDataOfClient(client, finishRegistrationRequest)
        clientRepository.save(client)
        logger.debug("client {} {} is saved", client.firstName, client.lastName)

        val creditDTO: CreditDto?
        try {
            creditDTO = restTemplateRequestsService.requestToCalculateCredit(scoringDataDTO)
        } catch (e: IllegalDataFromOtherMsException) {
            application =
                fillingDataServiceImpl.updateApplicationWithNewStatus(application, ApplicationStatus.CC_DENIED)
            applicationRepository.save(application)
            documentKafkaService.sendMessage(application, Theme.APPLICATION_DENIED)
            logger.debug("application with id={} is saved", application.applicationId)
            throw e
        }

        val credit = fillingDataServiceImpl.createCreditAfterCalculating(creditDTO, application)
        logger.info(
            "credit for {} {} with calculated",
            client.firstName, client.lastName
        )
        creditRepository.save(credit)
        logger.debug("credit with id={} is saved", credit.creditId)

        application.credit = credit
        application = fillingDataServiceImpl.updateApplicationWithNewStatus(application, ApplicationStatus.CC_APPROVED)
        applicationRepository.save(application)
        logger.debug("application with id={} is saved", application.applicationId)

        documentKafkaService.sendMessage(application, Theme.CREATE_DOCUMENTS)
    }

    override fun getApplicationDTOById(id: Long): ApplicationDto {
        val foundApplication = applicationRepository.findById(id)
        if (foundApplication.isEmpty) {
            notFound("Application with id $id not found")
        }
        val application = foundApplication.get()
        logger.info("application with id= {} received", application.applicationId)
        val applicationDTO = ApplicationDto(
            applicationId = application.applicationId,
            firstName = application.client?.firstName ?: nullException(NOT_NULL),
            lastName = application.client.lastName,
            middleName = application.client.middleName,
            amount = application.credit?.amount ?: nullException(NOT_NULL),
            term = application.credit?.term ?: nullException(NOT_NULL),
            monthlyPayment = application.credit?.monthlyPayment ?: nullException(NOT_NULL),
            rate = application.credit?.rate ?: nullException(NOT_NULL),
            psk = application.credit?.psk ?: nullException(NOT_NULL),
            paymentSchedule = application.credit?.paymentSchedule ?: nullException(NOT_NULL),
            insuranceEnable = application.credit?.insuranceEnable ?: nullException(NOT_NULL),
            salaryClient = application.credit?.salaryClient ?: nullException(NOT_NULL),
            creationDate = application.creationDate,
            signDate = application.signDate,
            sesCode = application.sesCode
        )
        logger.debug("applicationDto for {} {} create", applicationDTO.firstName, applicationDTO.lastName)
        return applicationDTO
    }
}