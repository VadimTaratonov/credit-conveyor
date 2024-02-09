package ru.taratonov.conveyor.service

import ru.taratonov.conveyor.dto.CreditDTO
import ru.taratonov.conveyor.dto.PaymentScheduleElement
import ru.taratonov.conveyor.dto.ScoringDataDTO
import java.math.BigDecimal

interface CreditCalculationService {
    fun calculateLoanParameters(scoringData: ScoringDataDTO, newRate: BigDecimal): CreditDTO

    fun calculateMonthlyPayment(amount: BigDecimal, rate: BigDecimal, term: Int): BigDecimal

    fun calculatePaymentSchedule(amount: BigDecimal, rate: BigDecimal, term: Int): List<PaymentScheduleElement>

    fun calculatePSK(amount: BigDecimal, term: Int, scheduleElements: List<PaymentScheduleElement>): BigDecimal

    fun calculateTotalAmount(amount: BigDecimal, isInsuranceEnabled: Boolean): BigDecimal
}