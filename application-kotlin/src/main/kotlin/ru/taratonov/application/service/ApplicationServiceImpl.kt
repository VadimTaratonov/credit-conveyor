package ru.taratonov.application.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ru.taratonov.application.dto.LoanApplicationRequestDto
import ru.taratonov.application.dto.LoanOfferDto

@Service
class ApplicationServiceImpl(
    private val restTemplateRequestsService: RestTemplateRequestsService
) : ApplicationService {
    private val logger = KotlinLogging.logger { }

    override fun getOffers(loanApplicationRequest: LoanApplicationRequestDto): List<LoanOfferDto> {
        logger.info(
            "Get loanApplicationRequestDTO and create new client with name - {}, surname - {}",
            loanApplicationRequest.firstName, loanApplicationRequest.lastName
        )
        return restTemplateRequestsService.requestToGetOffers(loanApplicationRequest)
    }

    override fun chooseOffer(loanOffer: LoanOfferDto) {
        logger.info("Selected offer is {}", loanOffer)
        restTemplateRequestsService.chooseOffer(loanOffer)
    }
}