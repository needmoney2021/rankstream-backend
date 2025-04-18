package com.rankstream.backend.domain.company.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.rankstream.backend.domain.company.entity.QCompany
import org.springframework.stereotype.Repository

@Repository
class CompanyQueryDslRepository(
    private val jpaQueryFactory: JPAQueryFactory
) {

    private val company = QCompany.company


}

fun QCompany.businessLicenseEquals(businessLicense: String?) = businessLicense?.let { this.businessLicense.eq(it) }
fun QCompany.idxEquals(idx: Long?) = idx?.let { this.idx.eq(it) }
