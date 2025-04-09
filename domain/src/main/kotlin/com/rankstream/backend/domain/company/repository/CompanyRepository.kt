package com.rankstream.backend.domain.company.repository

import com.rankstream.backend.domain.company.entity.Company
import org.springframework.data.jpa.repository.JpaRepository

interface CompanyRepository : JpaRepository<Company, Long> 