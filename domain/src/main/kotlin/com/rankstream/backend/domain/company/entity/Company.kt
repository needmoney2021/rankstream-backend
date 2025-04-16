package com.rankstream.backend.domain.company.entity

import com.rankstream.backend.domain.auditor.TimestampEntityListener
import com.rankstream.backend.domain.company.dto.request.CompanyRegistrationRequest
import com.rankstream.backend.domain.company.enums.BusinessType
import com.rankstream.backend.domain.company.enums.CommissionPlan
import com.rankstream.backend.domain.enums.State
import com.rankstream.backend.domain.member.entity.Member
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import java.util.*

@Entity
@Table(
    name = "company",
    indexes = [
        Index(name = "IDX_COMPANY_NAME", columnList = "company_name"),
        Index(name = "IDX_COMPANY_BUSINESS_TYPE", columnList = "business_type"),
        Index(name = "IDX_COMPANY_STATE", columnList = "state"),
        Index(name = "UIDX_COMPANY_LICENSE", columnList = "business_license", unique = true)
    ]
)
class Company(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null,

    @Column(length = 20, nullable = false)
    val businessLicense: String,

    @Column(length = 20, nullable = false)
    val representative: String,

    @Column(length = 20, nullable = false)
    val companyName: String,

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    val businessType: BusinessType,

    @Column(length = 20, nullable = true)
    var phone: String?,

    @Column(length = 10, nullable = true)
    var postalCode: String?,

    @Column(length = 100, nullable = true)
    var address: String?,

    @Column(length = 100, nullable = true)
    var addressDetail: String?,

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    var state: State,

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = true)
    var commissionPlan: CommissionPlan? = null,

    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], orphanRemoval = true)
    val members: MutableList<Member> = mutableListOf()
) : TimestampEntityListener() {

    companion object {
        fun fromCompanyRegistration(companyRegistrationRequest: CompanyRegistrationRequest): Company {
            val isCorp = companyRegistrationRequest.businessType == BusinessType.CORPORATION
            with(companyRegistrationRequest) {
                return Company(
                    businessLicense = businessLicense,
                    representative = if (isCorp) representative!! else name!!,
                    companyName = if (isCorp) companyName!! else name!!,
                    businessType = businessType,
                    phone = if (isCorp) companyPhone!! else phoneNumber!!,
                    postalCode = if (isCorp) companyPostalCode else postalCode,
                    address = if (isCorp) companyAddress else address,
                    addressDetail = if (isCorp) companyAddressDetail else addressDetail,
                    state = State.ACTIVE
                )
            }

        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false

        val otherCompany = when (other) {
            is HibernateProxy -> (other.hibernateLazyInitializer.implementation as? Company)
            is Company -> other
            else -> return false
        } ?: return false

        return idx != null && idx == otherCompany.idx
    }

    override fun hashCode(): Int = Objects.hash(idx)

    override fun toString(): String = "Company(idx=$idx, companyName='$companyName')"
}
