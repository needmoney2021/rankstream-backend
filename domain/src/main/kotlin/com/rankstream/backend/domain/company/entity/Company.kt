package com.rankstream.backend.domain.company.entity

import com.rankstream.backend.domain.auditor.TimestampEntityListener
import com.rankstream.backend.domain.enums.Hierarchy
import com.rankstream.backend.domain.enums.State
import com.rankstream.backend.domain.member.entity.Member
import jakarta.persistence.*
import jakarta.persistence.Index
import org.hibernate.proxy.HibernateProxy
import java.util.*

@Entity
@Table(
    name = "company",
    indexes = [
        Index(name = "IDX-COMPANY-NAME", columnList = "company_name"),
        Index(name = "IDX-COMPANY-STATE", columnList = "state"),
        Index(name = "UIDX-COMPANY-LICENSE", columnList = "business_license", unique = true)
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

    @Column(length = 20, nullable = false)
    var phone: String,

    @Column(length = 50, nullable = false)
    var email: String,

    @Column(length = 10, nullable = false)
    var postalCode: String,

    @Column(length = 100, nullable = false)
    var address: String,

    @Column(length = 100, nullable = false)
    var addressDetail: String,

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    var state: State,

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    var hierarchy: Hierarchy,

    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], orphanRemoval = true)
    val members: MutableList<Member> = mutableListOf()
) : TimestampEntityListener() {

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