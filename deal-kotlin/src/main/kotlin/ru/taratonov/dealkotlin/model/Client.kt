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
import ru.taratonov.dealkotlin.dto.EmploymentDto
import ru.taratonov.dealkotlin.dto.PassportDto
import ru.taratonov.dealkotlin.enums.Gender
import ru.taratonov.dealkotlin.enums.MaritalStatus
import java.time.LocalDate

@Entity
@Table(name = "client")
data class Client(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "client_id")
    val clientId: Long? = null,

    @Column(name = "first_name")
    val firstName: String? = null,

    @Column(name = "last_name")
    val lastName: String? = null,

    @Column(name = "middle_name")
    val middleName: String? = null,

    @Column(name = "birth_date")
    val birthDate: LocalDate? = null,

    @Column(name = "email")
    val email: String? = null,

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    var gender: Gender? = null,

    @Column(name = "marital_status")
    @Enumerated(EnumType.STRING)
    var maritalStatus: MaritalStatus? = null,

    @Column(name = "dependent_amount")
    var dependentAmount: Int? = null,

    @Column(name = "passport_id")
    @JdbcTypeCode(SqlTypes.JSON)
    var passportDTOId: PassportDto? = null,

    @Column(name = "employment_id")
    @JdbcTypeCode(SqlTypes.JSON)
    var employmentId: EmploymentDto? = null,

    @Column(name = "account")
    var account: String? = null,

    @OneToOne(mappedBy = "client")
    @JsonManagedReference(value = "client_application")
    val application: Application? = null
)
