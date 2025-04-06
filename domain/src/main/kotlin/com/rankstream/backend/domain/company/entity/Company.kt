package com.rankstream.backend.domain.company.entity

import com.rankstream.backend.domain.auditor.TimestampEntityListener
import com.rankstream.backend.domain.company.code.entity.Code
import com.rankstream.backend.domain.company.enums.CompanyState
import com.rankstream.backend.domain.company.enums.Hierarchy
import jakarta.persistence.*

@Entity
@Table(
    name = "company",
    indexes = [
        Index(name = "IDX_COMPANY_STATE", columnList = "state", unique = false),
        Index(name = "IDX_BUSINESS_LICENSE_ID", columnList = "business_licence_id", unique = true)
    ]
)
class Company(

    @Id
    @Column(name = "idx")
    val idx: Long? = null,

    @Column(name = "business_licence_id", nullable = false, length = 20)
    val businessLicenceId: String,

    @Column(name = "representative_name", nullable = false, length = 20)
    val representativeName: String,

    @Column(name = "company_name", nullable = false, length = 20)
    val companyName: String,

    @Column(name = "phone", nullable = false, length = 20)
    var phone: String,

    @Column(name = "email", nullable = false, length = 50)
    var email: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = [JoinColumn(name = "super_code"), JoinColumn(name = "sub_code")])
    val country: Code,

    @Column(name = "postal_code", nullable = false, length = 10)
    var postalCode: String,

    @Column(name = "address", nullable = false, length = 100)
    var address: String,

    @Column(name = "address_detail", nullable = false, length = 100)
    var addressDetail: String,

    @Column(name = "state", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    var state: CompanyState = CompanyState.ACTIVE,

    @Column(name = "hierarchy", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    var hierarchy: Hierarchy,

    @Column(name = "max_children", nullable = true)
    var maximumNumberOfChildren: Int = 2

) : TimestampEntityListener() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Company) return false

        if (businessLicenceId != other.businessLicenceId) return false

        return true
    }

    override fun hashCode(): Int {
        return businessLicenceId.hashCode()
    }

}
