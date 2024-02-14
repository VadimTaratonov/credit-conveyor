package ru.taratonov.application.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.taratonov.application.dto.LoanApplicationRequestDto
import ru.taratonov.application.dto.LoanOfferDto
import ru.taratonov.application.util.nullException

@Service
class RestTemplateRequestsServiceImpl(
    private val restTemplate: RestTemplate,
    @Value("\${custom.integration.deal.url.get.offers}") private val PATH_TO_DEAL_GET_OFFERS: String,
    @Value("\${custom.integration.deal.url.choose.offer}") private val PATH_TO_DEAL_CHOOSE_OFFER: String,
    @Value("\${custom.text.nonNull}") private val NOT_NULL: String
) : RestTemplateRequestsService {

    private val logger = KotlinLogging.logger { }

    override fun requestToGetOffers(loanApplicationRequest: LoanApplicationRequestDto): List<LoanOfferDto> {
        logger.debug("Request to get offer to deal with {}", loanApplicationRequest)

        val responseEntity = restTemplate.postForEntity(
            PATH_TO_DEAL_GET_OFFERS,
            loanApplicationRequest,
            Array<LoanOfferDto>::class.java
        )
        return responseEntity.body?.toList() ?: nullException(NOT_NULL)
    }

    override fun chooseOffer(loanOffer: LoanOfferDto) {
        logger.debug("Request to select offer to deal with {}", loanOffer)
        restTemplate.put(PATH_TO_DEAL_CHOOSE_OFFER, loanOffer)
    }
}