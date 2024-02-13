package ru.taratonov.dealkotlin.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.taratonov.dealkotlin.dto.ApplicationStatusHistoryDto
import ru.taratonov.dealkotlin.dto.CreditDto
import ru.taratonov.dealkotlin.dto.EmploymentDto
import ru.taratonov.dealkotlin.dto.FinishRegistrationRequestDto
import ru.taratonov.dealkotlin.dto.LoanApplicationRequestDto
import ru.taratonov.dealkotlin.dto.LoanOfferDto
import ru.taratonov.dealkotlin.dto.PassportDto
import ru.taratonov.dealkotlin.dto.PaymentScheduleElement
import ru.taratonov.dealkotlin.enums.ApplicationStatus
import ru.taratonov.dealkotlin.enums.ChangeType
import ru.taratonov.dealkotlin.enums.CreditStatus
import ru.taratonov.dealkotlin.enums.Gender
import ru.taratonov.dealkotlin.enums.MaritalStatus
import ru.taratonov.dealkotlin.model.Application
import ru.taratonov.dealkotlin.model.Client
import java.math.BigDecimal
import java.time.LocalDate

class FillingDataServiceTest {
    private val nonValueString = "Should not be null"
    private val fillingDataServiceImpl = FillingDataServiceImpl(
        NOT_NULL = nonValueString
    )
    private val client = Client(
        firstName = "Vadim",
        lastName = "Taratonov",
        middleName = "Nikolaevich",
        birthDate = LocalDate.parse("2001-02-10"),
        passportDTOId = PassportDto(
            series = "1111",
            number = "111111"
        )
    )
    private val finishRegistrationRequest = FinishRegistrationRequestDto(
        gender = Gender.MALE,
        passportIssueDate = LocalDate.parse("1990-01-01"),
        passportIssueBranch = "a",
        maritalStatus = MaritalStatus.SINGLE,
        dependentAmount = 1,
        account = "a",
        employment = EmploymentDto()
    )

    @Test
    fun `successful creation of client of a loan request`() {
        val loanApplicationRequest = LoanApplicationRequestDto(
            firstName = "Vadim",
            lastName = "Taratonov",
            middleName = "Nikolaevich",
            email = "email@email.com",
            birthdate = LocalDate.parse("2001-02-10"),
            passportSeries = "1111",
            passportNumber = "111111"
        )

        val client = fillingDataServiceImpl.createClientOfRequest(loanApplicationRequest)

        assertEquals(loanApplicationRequest.firstName, client.firstName)
        assertEquals(loanApplicationRequest.lastName, client.lastName)
        assertEquals(loanApplicationRequest.middleName, client.middleName)
        assertEquals(loanApplicationRequest.email, client.email)
        assertEquals(loanApplicationRequest.birthdate, client.birthDate)
        assertEquals(loanApplicationRequest.passportNumber, client.passportDTOId?.number)
        assertEquals(loanApplicationRequest.passportSeries, client.passportDTOId?.series)
    }

    @Test
    fun `successful creation of application of a loan request`() {
        val expectedApplication = Application(
            client = client,
            status = ApplicationStatus.PREAPPROVAL,
            creationDate = LocalDate.now(),
            applicationStatusHistoryDTO = mutableListOf(
                ApplicationStatusHistoryDto(
                    status = ApplicationStatus.PREAPPROVAL,
                    time = LocalDate.now(),
                    changeType = ChangeType.AUTOMATIC

                )
            )
        )

        val actualApplication = fillingDataServiceImpl.createApplicationOfRequest(client)

        assertEquals(expectedApplication, actualApplication)
    }

    @Test
    fun `successful updating application when choose offer`() {
        val loanOfferDto = LoanOfferDto()
        val application = Application(
            applicationStatusHistoryDTO = mutableListOf(
                ApplicationStatusHistoryDto(
                    status = ApplicationStatus.PREAPPROVAL,
                    time = LocalDate.now(),
                    changeType = ChangeType.AUTOMATIC

                )
            )
        )

        val actualApplication = fillingDataServiceImpl.updateApplicationWhenChooseOffer(application, loanOfferDto)

        assertEquals(2, actualApplication.applicationStatusHistoryDTO?.size)
        assertEquals(loanOfferDto, actualApplication.appliedOffer)
        assertEquals(ApplicationStatus.APPROVED, actualApplication.status)
    }

    @Test
    fun `unsuccessful updating application when choose offer and throw exception`() {
        assertThrows<NullPointerException> {
            fillingDataServiceImpl.updateApplicationWhenChooseOffer(Application(), LoanOfferDto())
        }
    }

    @Test
    fun `successful filling all information to scoringData`() {
        val application = Application(
            appliedOffer = LoanOfferDto(
                requestedAmount = BigDecimal.ONE,
                term = 7,
                isSalaryClient = true,
                isInsuranceEnabled = true
            )
        )
        val actual = fillingDataServiceImpl.fillAllInformationToScoringData(
            finishRegistrationRequest,
            client,
            application
        )

        assertEquals(client.firstName, actual.firstName)
        assertEquals(client.lastName, actual.lastName)
        assertEquals(client.middleName, actual.middleName)
        assertEquals(finishRegistrationRequest.gender, actual.gender)
        assertEquals(client.birthDate, actual.birthdate)
        assertEquals(finishRegistrationRequest.passportIssueBranch, actual.passportIssueBranch)
        assertEquals(finishRegistrationRequest.passportIssueDate, actual.passportIssueDate)
        assertEquals(finishRegistrationRequest.maritalStatus, actual.maritalStatus)
        assertEquals(finishRegistrationRequest.dependentAmount, actual.dependentAmount)
        assertEquals(finishRegistrationRequest.account, actual.account)
        assertEquals(application.appliedOffer?.requestedAmount, actual.amount)
        assertEquals(application.appliedOffer?.term, actual.term)
    }

    @Test
    fun `unsuccessful filling all information to scoringData and throw exception`() {
        assertThrows<NullPointerException> {
            fillingDataServiceImpl.fillAllInformationToScoringData(
                finishRegistrationRequest,
                client,
                Application()
            )
        }
    }

    @Test
    fun `successful creating credit after calculating`() {
        val creditDto = CreditDto(
            amount = BigDecimal.ONE,
            term = 1,
            monthlyPayment = BigDecimal.ONE,
            rate = BigDecimal.ONE,
            psk = BigDecimal.ONE,
            paymentSchedule = listOf(PaymentScheduleElement()),
            isSalaryClient = true,
            isInsuranceEnabled = true
        )

        val actualCredit = fillingDataServiceImpl.createCreditAfterCalculating(creditDto, Application())

        assertEquals(creditDto.amount, actualCredit.amount)
        assertEquals(creditDto.term, actualCredit.term)
        assertEquals(creditDto.monthlyPayment, actualCredit.monthlyPayment)
        assertEquals(creditDto.rate, actualCredit.rate)
        assertEquals(creditDto.psk, actualCredit.psk)
        assertEquals(creditDto.paymentSchedule, actualCredit.paymentSchedule)
        assertEquals(creditDto.isInsuranceEnabled, actualCredit.insuranceEnable)
        assertEquals(creditDto.isSalaryClient, actualCredit.salaryClient)
        assertEquals(CreditStatus.CALCULATED, actualCredit.creditStatus)
    }

    @Test
    fun `successful filling all data of client`() {
        val actualClient = fillingDataServiceImpl.fillAllDataOfClient(client, finishRegistrationRequest)

        assertEquals(finishRegistrationRequest.gender, actualClient.gender)
        assertEquals(finishRegistrationRequest.maritalStatus, actualClient.maritalStatus)
        assertEquals(finishRegistrationRequest.dependentAmount, actualClient.dependentAmount)
        assertEquals(finishRegistrationRequest.employment, actualClient.employmentId)
        assertEquals(client.passportDTOId, actualClient.passportDTOId)
    }

    @Test
    fun `successful updating application with new status`() {
        val application = Application(
            applicationStatusHistoryDTO = mutableListOf(
                ApplicationStatusHistoryDto(
                    status = ApplicationStatus.PREAPPROVAL,
                    time = LocalDate.now(),
                    changeType = ChangeType.AUTOMATIC

                )
            )
        )
        val newStatus = ApplicationStatus.CLIENT_DENIED

        val actualApplication = fillingDataServiceImpl.updateApplicationWithNewStatus(application, newStatus)

        assertEquals(2, actualApplication.applicationStatusHistoryDTO?.size)
    }

    @Test
    fun `unsuccessful updating application with new status and throw exception`() {
        val newStatus = ApplicationStatus.CLIENT_DENIED

        assertThrows<NullPointerException> {
            fillingDataServiceImpl.updateApplicationWithNewStatus(Application(), newStatus)
        }
    }
}