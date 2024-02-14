package ru.taratonov.application.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.taratonov.application.dto.LoanApplicationRequestDto
import ru.taratonov.application.dto.LoanOfferDto

class ApplicationServiceTest {
    private val restTemplateRequestsService = mockk<RestTemplateRequestsService>()
    private val applicationService = ApplicationServiceImpl(
        restTemplateRequestsService = restTemplateRequestsService
    )

    @Test
    fun `successful getting offers by loan request`() {
        val expectedOffer = LoanOfferDto()
        val expectedOffers = listOf(expectedOffer, expectedOffer)
        val loanApplicationRequest = LoanApplicationRequestDto()

        every { restTemplateRequestsService.requestToGetOffers(loanApplicationRequest) } returns expectedOffers

        val actualOffers = applicationService.getOffers(loanApplicationRequest)

        assertEquals(expectedOffers.size, actualOffers.size)
        assertEquals(expectedOffer, actualOffers[0])
        verify(exactly = 1) { restTemplateRequestsService.requestToGetOffers(loanApplicationRequest) }
    }

    @Test
    fun `successful choosing offer`() {
        val loanOffer = LoanOfferDto()
        every { restTemplateRequestsService.chooseOffer(loanOffer) } returns Unit
        applicationService.chooseOffer(loanOffer)
        verify(exactly = 1) { restTemplateRequestsService.chooseOffer(loanOffer) }
    }
}