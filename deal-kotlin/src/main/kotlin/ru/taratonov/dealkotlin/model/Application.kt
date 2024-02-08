package ru.taratonov.dealkotlin.model

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import ru.taratonov.dealkotlin.dto.ApplicationStatusHistoryDto
import ru.taratonov.dealkotlin.dto.LoanOfferDto
import ru.taratonov.dealkotlin.enums.ApplicationStatus
import java.time.LocalDate

@Entity
@Table(name = "application")
data class Application(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "application_id")
    val applicationId: Long? = null,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    @JsonBackReference(value = "client_application")
    val client: Client? = null,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "credit_id", referencedColumnName = "credit_id")
    @JsonBackReference(value = "credit_application")
    var credit: Credit? = null,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    var status: ApplicationStatus? = null,

    @Column(name = "creation_date")
    var creationDate: LocalDate? = null,

    @Column(name = "applied_offer")
    @JdbcTypeCode(SqlTypes.JSON)
    var appliedOffer: LoanOfferDto? = null,

    @Column(name = "sign_date")
    var signDate: LocalDate? = null,

    @Column(name = "ses_code")
    var sesCode: Int? = null,

    @Column(name = "status_history")
    @JdbcTypeCode(SqlTypes.JSON)
    var applicationStatusHistoryDTO: MutableList<ApplicationStatusHistoryDto>? = null
)
