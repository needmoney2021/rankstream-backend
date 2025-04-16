package com.rankstream.backend.domain.admin.service

import com.rankstream.backend.domain.admin.entity.Administrator
import com.rankstream.backend.domain.admin.repository.AdministratorQueryDslRepository
import com.rankstream.backend.domain.admin.repository.AdministratorRepository
import com.rankstream.backend.exception.NotFoundException
import com.rankstream.backend.exception.enums.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AdministratorService(
    private val administratorRepository: AdministratorRepository,
    private val administratorQueryDslRepository: AdministratorQueryDslRepository
) {

    fun findByUserId(userId: String): Administrator {
        return administratorQueryDslRepository.findByUserId(userId)
            ?: throw NotFoundException("Administrator with id $userId not found.", ErrorCode.NOT_FOUND, userId)
    }

}
