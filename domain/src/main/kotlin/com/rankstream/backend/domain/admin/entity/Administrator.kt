package com.rankstream.backend.domain.admin.entity

import com.rankstream.backend.domain.admin.dto.request.AdministratorRegistrationCommand
import com.rankstream.backend.domain.auditor.TimestampEntityListener
import com.rankstream.backend.domain.company.dto.request.CompanyRegistrationRequest
import com.rankstream.backend.domain.company.entity.Company
import com.rankstream.backend.domain.company.enums.BusinessType
import com.rankstream.backend.domain.enums.State
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import java.util.*

@Entity
@Table(
    name = "admin",
    indexes = [
        Index(name = "IDX_ADMINISTRATOR_COMPANY", columnList = "company_idx"),
        Index(name = "UIDX_ADMINISTRATOR_USERID", columnList = "company_idx, user_id"),
        Index(name = "IDX_ADMINISTRATOR_STATE", columnList = "state"),
        Index(name = "IDX_ADMINISTRATOR_NAME", columnList = "user_name"),
        Index(name = "IDX_ADMINISTRATOR_USERID", columnList = "user_id")
    ]
)
class Administrator(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null,

    @ManyToOne
    @JoinColumn(name = "company_idx", nullable = false)
    val company: Company,

    @Column(length = 50, nullable = false)
    val userId: String,

    @Column(length = 100, nullable = false)
    var password: String,

    @Column(length = 10, nullable = false)
    val userName: String,

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    var state: State,

    @Column(length = 20, nullable = false)
    var department: String
) : TimestampEntityListener() {

    companion object {

        fun create(command: AdministratorRegistrationCommand, company: Company): Administrator {
            return Administrator(
                company = company,
                userId = command.id,
                password = command.encodedPassword,
                userName = command.name,
                department = command.department,
                state = State.ACTIVE
            )
        }

        fun fromCompanyRegistration(companyRegistrationRequest: CompanyRegistrationRequest, company: Company): Administrator {
            val isCorp = companyRegistrationRequest.businessType == BusinessType.CORPORATION
            with (companyRegistrationRequest) {
                return Administrator(
                    company = company,
                    userId = email,
                    password = password,
                    userName = if (isCorp) {
                        representative!!
                    } else {
                        name!!
                    },
                    state = State.ACTIVE,
                    department = "Supervisor"
                )
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false

        val otherAdministrator = when (other) {
            is HibernateProxy -> (other.hibernateLazyInitializer.implementation as? Administrator)
            is Administrator -> other
            else -> return false
        } ?: return false

        return idx != null && idx == otherAdministrator.idx
    }

    override fun hashCode(): Int = Objects.hash(idx)

}
