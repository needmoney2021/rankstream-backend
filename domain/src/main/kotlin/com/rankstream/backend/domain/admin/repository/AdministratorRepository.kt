package com.rankstream.backend.domain.admin.repository

import com.rankstream.backend.domain.admin.entity.Administrator
import com.rankstream.backend.domain.company.entity.Company
import org.springframework.data.jpa.repository.JpaRepository

interface AdministratorRepository : JpaRepository<Administrator, Long> {

    fun existsByCompanyAndUserId(company: Company, userId: String): Boolean

}
