package com.rankstream.backend.auth.service

import com.rankstream.backend.auth.user.AdministratorDetails
import com.rankstream.backend.domain.admin.repository.AdministratorQueryDslRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AdministratorDetailsService(
    private val administratorQueryDslRepository: AdministratorQueryDslRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails? {
        val administrator = administratorQueryDslRepository.findByIdx(username.toLong())
            ?: throw UsernameNotFoundException(username)
        return AdministratorDetails(administrator)
    }

}
