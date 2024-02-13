package ru.taratonov.dealkotlin.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.taratonov.dealkotlin.dto.ApplicationDto
import ru.taratonov.dealkotlin.dto.CreditDto
import ru.taratonov.dealkotlin.dto.FinishRegistrationRequestDto
import ru.taratonov.dealkotlin.dto.LoanApplicationRequestDto
import ru.taratonov.dealkotlin.dto.LoanOfferDto
import ru.taratonov.dealkotlin.dto.PaymentScheduleElement
import ru.taratonov.dealkotlin.dto.ScoringDataDto
import ru.taratonov.dealkotlin.exception.NotFoundException
import ru.taratonov.dealkotlin.model.Application
import ru.taratonov.dealkotlin.model.Client
import ru.taratonov.dealkotlin.model.Credit
import ru.taratonov.dealkotlin.repository.ApplicationRepository
import ru.taratonov.dealkotlin.repository.ClientRepository
import ru.taratonov.dealkotlin.repository.CreditRepository
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class DealServiceTest {

    private val clientRepository = mockk<ClientRepository>()
    private val applicationRepository = mockk<ApplicationRepository>()
    private val creditRepository = mockk<CreditRepository>()
    private val restTemplateRequestsService = mockk<RestTemplateRequestsService>()
    private val fillingDataService = mockk<FillingDataService>()
    private val documentKafkaService = mockk<DocumentKafkaService>()
    private val nonValueString = "Should not be null"
    private val dealService = DealServiceImpl(
        creditRepository = creditRepository,
        clientRepository = clientRepository,
        applicationRepository = applicationRepository,
        restTemplateRequestsService = restTemplateRequestsService,
        fillingDataService = fillingDataService,
        documentKafkaService = documentKafkaService,
        NOT_NULL = nonValueString
    )

    @Test
    fun `get offers for person by request`() {
        val loanApplicationRequest = LoanApplicationRequestDto()
        val client = Client()
        val application = Application()
        val loanOffers = listOf(LoanOfferDto(), LoanOfferDto())

        every { fillingDataService.createClientOfRequest(loanApplicationRequest) } returns client
        every { fillingDataService.createApplicationOfRequest(client) } returns application
        every { applicationRepository.save(application) } returns application
        every { restTemplateRequestsService.requestToGetOffers(loanApplicationRequest) } returns loanOffers

        val actual = dealService.getOffers(loanApplicationRequest)

        assertEquals(loanOffers.size, actual.size)
        verify(exactly = 1) { fillingDataService.createClientOfRequest(loanApplicationRequest) }
        verify(exactly = 1) { clientRepository.save(client) }
        verify(exactly = 1) { fillingDataService.createApplicationOfRequest(client) }
        verify(exactly = 1) { applicationRepository.save(application) }
        verify(exactly = 1) { restTemplateRequestsService.requestToGetOffers(loanApplicationRequest) }
    }

    @Test
    fun `unsuccessful choosing offer because application not found`() {
        val applicationId = 1L
        val loanOffer = LoanOfferDto(applicationId = applicationId)

        every { applicationRepository.findById(applicationId) } returns Optional.empty()

        assertThrows<NotFoundException> { dealService.chooseOffer(loanOffer) }

        verify(exactly = 1) { applicationRepository.findById(applicationId) }
    }

    @Test
    fun `successful choosing offer`() {
        val applicationId = 1L
        val loanOffer = LoanOfferDto(applicationId = applicationId)
        val application = Application()

        every { applicationRepository.findById(applicationId) } returns Optional.of(application)
        every { fillingDataService.updateApplicationWhenChooseOffer(application, loanOffer) } returns application
        every { applicationRepository.save(application) } returns application
        every { documentKafkaService.sendMessage(application, any()) } returns Unit

        dealService.chooseOffer(loanOffer)

        verify(exactly = 1) { applicationRepository.findById(applicationId) }
        verify(exactly = 1) { applicationRepository.save(application) }
        verify(exactly = 1) { fillingDataService.updateApplicationWhenChooseOffer(application, loanOffer) }
    }

    @Test
    fun `unsuccessful calculating credit because application not found`() {
        val applicationId = 1L
        val finishRegistrationRequestDto = FinishRegistrationRequestDto()

        every { applicationRepository.findById(applicationId) } returns Optional.empty()

        assertThrows<NotFoundException> { dealService.calculateCredit(finishRegistrationRequestDto, applicationId) }

        verify(exactly = 1) { applicationRepository.findById(applicationId) }
    }

    @Test
    fun `successful calculating credit`() {
        val applicationId = 1L
        val finishRegistrationRequestDto = FinishRegistrationRequestDto()
        val client = Client()
        val application = Application(
            client = client
        )
        val scoringDataDto = ScoringDataDto()
        val creditDto = CreditDto()
        val credit = Credit()

        every { applicationRepository.findById(applicationId) } returns Optional.of(application)
        every {
            fillingDataService.fillAllInformationToScoringData(
                finishRegistrationRequestDto,
                client,
                application
            )
        } returns scoringDataDto
        every { fillingDataService.fillAllDataOfClient(client, finishRegistrationRequestDto) } returns client
        every { restTemplateRequestsService.requestToCalculateCredit(scoringDataDto) } returns creditDto
        every { fillingDataService.createCreditAfterCalculating(creditDto, application) } returns credit
        every { fillingDataService.updateApplicationWithNewStatus(application, any()) } returns application
        every { clientRepository.save(client) } returns client
        every { creditRepository.save(credit) } returns credit
        every { applicationRepository.save(application) } returns application
        every { documentKafkaService.sendMessage(application, any()) } returns Unit

        dealService.calculateCredit(finishRegistrationRequestDto, applicationId)

        verify(exactly = 1) { applicationRepository.findById(applicationId) }
        verify(exactly = 1) {
            fillingDataService.fillAllInformationToScoringData(
                finishRegistrationRequestDto,
                client,
                application
            )
        }
        verify(exactly = 1) { fillingDataService.fillAllDataOfClient(client, finishRegistrationRequestDto) }
        verify(exactly = 1) { restTemplateRequestsService.requestToCalculateCredit(scoringDataDto) }
        verify(exactly = 1) { fillingDataService.createCreditAfterCalculating(creditDto, application) }
        verify(exactly = 1) { fillingDataService.updateApplicationWithNewStatus(application, any()) }
    }

    @Test
    fun `get applicationDto by application id`() {
        val id = 1L
        val client = Client(
            firstName = "name",
            lastName = "lastname",
            middleName = "middlename"
        )
        val credit = Credit(
            amount = BigDecimal.ONE,
            term = 1,
            monthlyPayment = BigDecimal.ONE,
            rate = BigDecimal.ONE,
            psk = BigDecimal.ONE,
            paymentSchedule = listOf(PaymentScheduleElement()),
            insuranceEnable = true,
            salaryClient = true
        )
        val application = Application(
            applicationId = id,
            client = client,
            credit = credit,
            creationDate = LocalDate.MIN,
            signDate = LocalDate.MIN,
            sesCode = 1111
        )

        every { applicationRepository.findById(id) } returns Optional.of(application)

        val expectedApplicationDto = ApplicationDto(
            applicationId = id,
            firstName = application.client?.firstName,
            lastName = application.client?.lastName,
            middleName = application.client?.middleName,
            amount = application.credit?.amount,
            term = application.credit?.term,
            monthlyPayment = application.credit?.monthlyPayment,
            rate = application.credit?.rate,
            psk = application.credit?.psk,
            paymentSchedule = application.credit?.paymentSchedule,
            insuranceEnable = application.credit?.insuranceEnable,
            salaryClient = application.credit?.salaryClient,
            creationDate = application.creationDate,
            signDate = application.signDate,
            sesCode = application.sesCode
        )

        val actualApplicationDto = dealService.getApplicationDTOById(id)

        assertEquals(expectedApplicationDto, actualApplicationDto)
    }
}