package ru.taratonov.conveyor.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import ru.taratonov.conveyor.dto.LoanApplicationRequestDTO
import ru.taratonov.conveyor.dto.LoanOfferDTO

@Service
class OfferServiceImpl(
    private val creditCalculationService: CreditCalculationService,
    private val scoringService: ScoringService
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

        val amount = loanApplicationRequest.amount
        val term = loanApplicationRequest.term
        val rate = scoringService.scoringPerson(isInsuranceEnabled, isSalaryClient)
        val totalAmount = creditCalculationService.calculateTotalAmount(amount, isInsuranceEnabled)

        val loanOfferDTO = LoanOfferDTO(
            0L,
            amount,
            totalAmount,
            term,
            creditCalculationService.calculateMonthlyPayment(totalAmount, rate, term),
            rate,
            isInsuranceEnabled,
            isSalaryClient
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