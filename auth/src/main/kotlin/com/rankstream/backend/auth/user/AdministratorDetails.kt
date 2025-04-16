package com.rankstream.backend.auth.user

import com.rankstream.backend.domain.admin.entity.Administrator
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import com.rankstream.backend.domain.enums.State

class AdministratorDetails(
    val administrator: Administrator
) : UserDetails {

    override fun getUsername(): String = administrator.userId
    override fun getPassword(): String = administrator.password
    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = administrator.state == State.ACTIVE

}
