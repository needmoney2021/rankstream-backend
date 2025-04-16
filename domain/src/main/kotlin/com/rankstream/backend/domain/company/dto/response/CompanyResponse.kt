package com.rankstream.backend.domain.company.dto.response

import com.rankstream.backend.domain.admin.entity.Administrator
import com.rankstream.backend.domain.company.entity.Company

data class CompanyRegistrationResponse(
    val companyIndex: Long,
    val companyName: String,
    val representative: String?,
    val administratorEmail: String
) {
    companion object {
        fun fromCompanyAndAdministrator(company: Company, administrator: Administrator): CompanyRegistrationResponse {
            return CompanyRegistrationResponse(
                company.idx!!,
                company.companyName,
                company.representative,
                administrator.userId
            )
        }
    }
}
