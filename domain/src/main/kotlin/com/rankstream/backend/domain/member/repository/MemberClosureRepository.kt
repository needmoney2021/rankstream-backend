package com.rankstream.backend.domain.member.repository

import com.rankstream.backend.domain.member.entity.MemberClosure
import org.springframework.data.jpa.repository.JpaRepository

interface MemberClosureRepository : JpaRepository<MemberClosure, Long> 