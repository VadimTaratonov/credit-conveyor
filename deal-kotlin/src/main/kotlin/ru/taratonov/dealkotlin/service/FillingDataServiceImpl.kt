package ru.taratonov.dealkotlin.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.taratonov.dealkotlin.dto.ApplicationStatusHistoryDto
import ru.taratonov.dealkotlin.dto.CreditDto
import ru.taratonov.dealkotlin.dto.FinishRegistrationRequestDto
import ru.taratonov.dealkotlin.dto.LoanApplicationRequestDto
import ru.taratonov.dealkotlin.dto.LoanOfferDto
import ru.taratonov.dealkotlin.dto.PassportDto
import ru.taratonov.dealkotlin.dto.ScoringDataDto
import ru.taratonov.dealkotlin.enums.ApplicationStatus
import ru.taratonov.dealkotlin.enums.ChangeType
import ru.taratonov.dealkotlin.enums.CreditStatus
import ru.taratonov.dealkotlin.model.Application
import ru.taratonov.dealkotlin.model.Client
import ru.taratonov.dealkotlin.model.Credit
import ru.taratonov.dealkotlin.util.nullException
import java.time.LocalDate

@Service
class FillingDataServiceImpl(
    @Value("\${text.nonNull}") private val NOT_NULL: String
) : FillingDataService {
    private val logger = KotlinLogging.logger { }

    override fun createClientOfRequest(loanApplicationRequest: LoanApplicationRequestDto): Client {
        logger.debug("Start create new client with data from {}", loanApplicationRequest)
        return Client(
            firstName = loanApplicationRequest.firstName,
            lastName = loanApplicationRequest.lastName,
            middleName = loanApplicationRequest.middleName,
            email = loanApplicationRequest.email,
            birthDate = loanApplicationRequest.birthdate,
            passportDTOId = PassportDto(
                series = loanApplicationRequest.passportSeries,
                number = loanApplicationRequest.passportNumber
            )
        )
    }

    override fun createApplicationOfRequest(client: Client): Application {
        logger.debug("Start create new application with {}", client)
        return Application(
            client = client,
            status = ApplicationStatus.PREAPPROVAL,
            applicationStatusHistoryDTO = mutableListOf(
                ApplicationStatusHistoryDto(
                    status = ApplicationStatus.PREAPPROVAL,
                    time = LocalDate.now(),
                    changeType = ChangeType.AUTOMATIC
                )
            ),
            creationDate = LocalDate.now()
        )
    }

    override fun updateApplicationWhenChooseOffer(application: Application, loanOffer: LoanOfferDto): Application {
        logger.debug("Start update application with {} and {}", application, loanOffer)
        val applicationStatusHistory = application.applicationStatusHistoryDTO ?: nullException(NOT_NULL)
        applicationStatusHistory.add(
            ApplicationStatusHistoryDto(
                status = ApplicationStatus.APPROVED,
                time = LocalDate.now(),
                changeType = ChangeType.AUTOMATIC
            )
        )

        application.appliedOffer = loanOffer
        application.status = ApplicationStatus.APPROVED
        application.applicationStatusHistoryDTO = applicationStatusHistory

        return application
    }

    override fun fillAllInformationToScoringData(
        finishRegistrationRequest: FinishRegistrationRequestDto,
        client: Client,
        application: Application
    ): ScoringDataDto {
        logger.debug("Start filling scoringData with {}, {} and {}", finishRegistrationRequest, client, application)
        return ScoringDataDto(
            amount = application.appliedOffer?.requestedAmount ?: nullException(NOT_NULL),
            term = application.appliedOffer?.term ?: nullException(NOT_NULL),
            firstName = client.firstName,
            lastName = client.lastName,
            middleName = client.middleName,
            gender = finishRegistrationRequest.gender,
            birthdate = client.birthDate,
            passportSeries = client.passportDTOId?.series ?: nullException(NOT_NULL),
            passportNumber = client.passportDTOId?.number ?: nullException(NOT_NULL),
            passportIssueDate = finishRegistrationRequest.passportIssueDate,
            passportIssueBranch = finishRegistrationRequest.passportIssueBranch,
            maritalStatus = finishRegistrationRequest.maritalStatus,
            dependentAmount = finishRegistrationRequest.dependentAmount,
            employment = finishRegistrationRequest.employment,
            account = finishRegistrationRequest.account,
            isInsuranceEnabled = application.appliedOffer?.isInsuranceEnabled ?: nullException(NOT_NULL),
            isSalaryClient = application.appliedOffer?.isSalaryClient ?: nullException(NOT_NULL)
        )
    }

    override fun createCreditAfterCalculating(creditDto: CreditDto, application: Application): Credit {
        logger.debug("Start create credit with {} and {}", creditDto, application)
        return Credit(
            amount = creditDto.amount,
            term = creditDto.term,
            monthlyPayment = creditDto.monthlyPayment,
            rate = creditDto.rate,
            psk = creditDto.psk,
            paymentSchedule = creditDto.paymentSchedule,
            insuranceEnable = creditDto.isInsuranceEnabled,
            salaryClient = creditDto.isSalaryClient,
            application = application,
            creditStatus = CreditStatus.CALCULATED
        )
    }

    override fun fillAllDataOfClient(
        client: Client,
        finishRegistrationRequest: FinishRegistrationRequestDto
    ): Client {
        logger.debug("Start update client with {} and {}", client, finishRegistrationRequest)
        val newPassport = client.passportDTOId ?: nullException(NOT_NULL)
        newPassport.issueDate = finishRegistrationRequest.passportIssueDate
        newPassport.issueBranch = finishRegistrationRequest.passportIssueBranch

        client.gender = finishRegistrationRequest.gender
        client.maritalStatus = finishRegistrationRequest.maritalStatus
        client.dependentAmount = finishRegistrationRequest.dependentAmount
        client.employmentId = finishRegistrationRequest.employment
        client.account = finishRegistrationRequest.account
        client.passportDTOId = newPassport

        return client
    }

    override fun updateApplicationWithNewStatus(
        application: Application,
        applicationStatus: ApplicationStatus
    ): Application {
        logger.debug("Start update application with new status {} ", applicationStatus)
        val applicationStatusHistory = application.applicationStatusHistoryDTO ?: nullException(NOT_NULL)
        applicationStatusHistory.add(
            ApplicationStatusHistoryDto(
                time = LocalDate.now(),
                changeType = ChangeType.AUTOMATIC,
                status = applicationStatus
            )
        )
        application.applicationStatusHistoryDTO = applicationStatusHistory
        application.status = applicationStatus
        return application
    }
}