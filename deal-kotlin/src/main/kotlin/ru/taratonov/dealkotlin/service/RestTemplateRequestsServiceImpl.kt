package ru.taratonov.dealkotlin.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.taratonov.dealkotlin.dto.CreditDto
import ru.taratonov.dealkotlin.dto.LoanApplicationRequestDto
import ru.taratonov.dealkotlin.dto.LoanOfferDto
import ru.taratonov.dealkotlin.dto.ScoringDataDto
import ru.taratonov.dealkotlin.util.nullException
import java.util.*

@Service
class RestTemplateRequestsServiceImpl(
    private val restTemplate: RestTemplate,
    @Value("\${custom.integration.conveyor.get.offers}") private val PATH_TO_CONVEYOR_GET_OFFERS: String,
    @Value("\${custom.integration.conveyor.calculate.credit}") private val PATH_TO_CONVEYOR_CALCULATE_CREDIT: String,
    @Value("\${text.nonNull}") private val NOT_NULL: String
) : RestTemplateRequestsService {
    private val logger = KotlinLogging.logger { }

    override fun requestToGetOffers(loanApplicationRequest: LoanApplicationRequestDto): List<LoanOfferDto> {
        logger.info("request to get offer to conveyor with {}", loanApplicationRequest)
        val responseEntity = restTemplate.postForEntity(
            PATH_TO_CONVEYOR_GET_OFFERS,
            loanApplicationRequest,
            Array<LoanOfferDto>::class.java
        )
        return responseEntity.body?.toList() ?: nullException(NOT_NULL)
    }

    override fun requestToCalculateCredit(scoringData: ScoringDataDto): CreditDto {
        logger.info("request to calculate credit to conveyor with {}", scoringData)
        val responseEntity = restTemplate.postForEntity(
            PATH_TO_CONVEYOR_CALCULATE_CREDIT,
            scoringData,
            CreditDto::class.java
        )
        return responseEntity.body ?: nullException(NOT_NULL)
    }
}