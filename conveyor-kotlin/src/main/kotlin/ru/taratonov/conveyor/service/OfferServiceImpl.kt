package ru.taratonov.conveyor.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.taratonov.conveyor.dto.LoanApplicationRequestDTO
import ru.taratonov.conveyor.dto.LoanOfferDTO
import ru.taratonov.conveyor.util.error

@Service
class OfferServiceImpl(
    private val creditCalculationService: CreditCalculationService,
    private val scoringService: ScoringService,
    @Value("\${custom.text.nonNull}") private val NON_VALUE: String
) : OfferService {

    private val logger = KotlinLogging.logger { }

    override fun createOffers(loanApplicationRequest: LoanApplicationRequestDTO): List<LoanOfferDTO> {
        logger.debug("!REQUEST FOR GENERATING OFFERS RECEIVED!")
        return mutableListOf(
            generateOffer(loanApplicationRequest, isInsuranceEnabled = false, isSalaryClient = false),
            generateOffer(loanApplicationRequest, isInsuranceEnabled = false, isSalaryClient = true),
            generateOffer(loanApplicationRequest, isInsuranceEnabled = true, isSalaryClient = false),
            generateOffer(loanApplicationRequest, isInsuranceEnabled = true, isSalaryClient = true)
        )
    }

    private fun generateOffer(
        loanApplicationRequest: LoanApplicationRequestDTO,
        isInsuranceEnabled: Boolean,
        isSalaryClient: Boolean
    ): LoanOfferDTO {
        logger.debug(
            "Start generate offer for {} with isInsuranceEnabled - {} and isSalaryClient - {}",
            loanApplicationRequest.firstName, isInsuranceEnabled, isSalaryClient
        )

        val amount = loanApplicationRequest.amount ?: error(NON_VALUE)
        val term = loanApplicationRequest.term ?: error(NON_VALUE)
        val rate = scoringService.scoringPerson(isInsuranceEnabled, isSalaryClient)
        val totalAmount = creditCalculationService.calculateTotalAmount(amount, isInsuranceEnabled)

        val loanOfferDTO = LoanOfferDTO(
            applicationId = 0L,
            requestedAmount = amount,
            totalAmount = totalAmount,
            term = term,
            monthlyPayment = creditCalculationService.calculateMonthlyPayment(totalAmount, rate, term),
            rate = rate,
            isInsuranceEnabled = isInsuranceEnabled,
            isSalaryClient = isSalaryClient
        )

        logger.info(
            "Offer is ready. " +
                    "Calculated data: id - {}, requestedAmount - {}, totalAmount - {}, term - {}, " +
                    "monthlyPayment - {}, rate - {}, isInsuranceEnabled - {}, isSalaryClient - {}",
            loanOfferDTO.applicationId, loanOfferDTO.requestedAmount,
            loanOfferDTO.totalAmount, loanOfferDTO.term, loanOfferDTO.monthlyPayment,
            loanOfferDTO.rate, loanOfferDTO.isInsuranceEnabled, loanOfferDTO.isSalaryClient
        )
        return loanOfferDTO
    }
}