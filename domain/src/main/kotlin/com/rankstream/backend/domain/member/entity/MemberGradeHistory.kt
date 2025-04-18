package com.rankstream.backend.domain.member.entity

import com.rankstream.backend.domain.grade.entity.Grade
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(
    name = "member_grade_history",
    indexes = [
        Index(name = "IDX_MEMBER_GRADE_HISTORY_MEMBER", columnList = "member_idx"),
        Index(name = "IDX_MEMBER_GRADE_HISTORY_ISSUED_AT", columnList = "issued_at")
    ]
)
class MemberGradeHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_grade", nullable = false)
    val previous: Grade,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_grade", nullable = false)
    val changed: Grade,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx", nullable = false)
    val member: Member,

    @Column(nullable = false)
    val issuedAt: LocalDateTime
)
