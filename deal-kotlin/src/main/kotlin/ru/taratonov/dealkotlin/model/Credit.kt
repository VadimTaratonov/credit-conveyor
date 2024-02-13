package ru.taratonov.dealkotlin.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import ru.taratonov.dealkotlin.dto.PaymentScheduleElement
import ru.taratonov.dealkotlin.enums.CreditStatus
import java.math.BigDecimal

@Entity
@Table(name = "credit")
data class Credit(

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "credit_id")
    val creditId: Long? = null,

    @Column(name = "amount")
    val amount: BigDecimal? = null,

    @Column(name = "term")
    val term: Int? = null,

    @Column(name = "monthly_payment")
    val monthlyPayment: BigDecimal? = null,

    @Column(name = "rate")
    val rate: BigDecimal? = null,

    @Column(name = "psk")
    val psk: BigDecimal? = null,

    @Column(name = "payment_schedule")
    @JdbcTypeCode(SqlTypes.JSON)
    val paymentSchedule: List<PaymentScheduleElement>? = null,

    @Column(name = "insurance_enable")
    val insuranceEnable: Boolean? = null,

    @Column(name = "salary_client")
    val salaryClient: Boolean? = null,

    @Column(name = "credit_status")
    @Enumerated(EnumType.STRING)
    var creditStatus: CreditStatus? = null,

    @OneToOne(mappedBy = "credit")
    @JsonManagedReference(value = "credit_application")
    val application: Application? = null
)
