package com.rankstream.backend.domain.admin.repository

import com.rankstream.backend.domain.admin.entity.Administrator
import org.springframework.data.jpa.repository.JpaRepository

interface AdministratorRepository : JpaRepository<Administrator, Long> {
}
