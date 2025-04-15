package com.rankstream.backend.auth.service

import com.rankstream.backend.auth.user.MemberDetails
import com.rankstream.backend.domain.member.repository.MemberQueryDslRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MemberDetailsService(
    private val memberQueryDslRepository: MemberQueryDslRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails? {
        val member = memberQueryDslRepository.findMemberByIdx(username.toLong())
            ?: throw UsernameNotFoundException(username)
        return MemberDetails(member)
    }

}
