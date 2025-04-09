package com.rankstream.backend.domain.member.repository

import com.rankstream.backend.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> 