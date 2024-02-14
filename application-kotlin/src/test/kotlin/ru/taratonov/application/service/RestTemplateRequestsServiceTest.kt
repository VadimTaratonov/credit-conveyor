package ru.taratonov.application.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.test.web.client.response.MockRestResponseCreators
import org.springframework.web.client.RestTemplate
import ru.taratonov.application.dto.LoanApplicationRequestDto
import ru.taratonov.application.dto.LoanOfferDto
import java.math.BigDecimal
import java.net.URI
import java.time.LocalDate

@SpringBootTest
@TestPropertySource(locations = ["/application-test.yml"])
class RestTemplateRequestsServiceTest(
    @Value("\${custom.text.nonNull}")
    private val NOT_NULL: String,
    @Value("\${custom.integration.deal.url.get.offers}")
    private val PATH_TO_DEAL_GET_OFFERS: String,
    @Value("\${custom.integration.deal.url.choose.offer}")
    private val PATH_TO_DEAL_CHOOSE_OFFER: String,
) {

    private lateinit var mockServer: MockRestServiceServer
    private val restTemplate = RestTemplate()
    private val restTemplateRequestsService = RestTemplateRequestsServiceImpl(
        restTemplate = restTemplate,
        PATH_TO_DEAL_GET_OFFERS = PATH_TO_DEAL_GET_OFFERS,
        PATH_TO_DEAL_CHOOSE_OFFER = PATH_TO_DEAL_CHOOSE_OFFER,
        NOT_NULL = NOT_NULL
    )
    private val objectMapper = ObjectMapper()
    private val loanApplicationRequest = LoanApplicationRequestDto(
        amount = BigDecimal.valueOf(10000),
        term = 9,
        birthdate = LocalDate.EPOCH
    )
    private val loanOffer = LoanOfferDto(
        term = 9,
        requestedAmount = BigDecimal.valueOf(10000)
    )

    @BeforeEach
    fun init() {
        mockServer = MockRestServiceServer.createServer(restTemplate)
    }

    @Test
    fun `successful request to get offers`() {
        val expectedList = listOf(loanOffer, loanOffer)

        mockServer.expect(
            ExpectedCount.once(),
            MockRestRequestMatchers.requestTo(URI(PATH_TO_DEAL_GET_OFFERS))
        )
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andRespond(
                MockRestResponseCreators.withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(expectedList))
            )

        val actualList = restTemplateRequestsService.requestToGetOffers(loanApplicationRequest)

        assertEquals(expectedList.size, actualList.size)
        assertEquals(loanApplicationRequest.amount, actualList[0].requestedAmount)
        assertEquals(loanApplicationRequest.term, actualList[0].term)
    }

    @Test
    fun `unsuccessful request to get offers and throw null exception`() {
        mockServer.expect(
            ExpectedCount.once(),
            MockRestRequestMatchers.requestTo(URI(PATH_TO_DEAL_GET_OFFERS))
        )
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andRespond(
                MockRestResponseCreators.withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("")
            )

        assertThrows<NullPointerException> { restTemplateRequestsService.requestToGetOffers(loanApplicationRequest) }
    }

    @Test
    fun `successful choosing offer`() {
        mockServer.expect(
            ExpectedCount.once(),
            MockRestRequestMatchers.requestTo(URI(PATH_TO_DEAL_CHOOSE_OFFER))
        )
            .andExpect(MockRestRequestMatchers.method(HttpMethod.PUT))
            .andRespond(
                MockRestResponseCreators.withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("")
            )

        restTemplateRequestsService.chooseOffer(loanOffer)
    }
}