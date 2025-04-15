package com.rankstream.backend.auth.user

import com.rankstream.backend.domain.member.entity.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import com.rankstream.backend.domain.enums.State

class MemberDetails(
    val member: Member
) : UserDetails {

    override fun getUsername(): String = member.memberId
    override fun getPassword(): String = member.password
    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = member.state == State.ACTIVE

}
