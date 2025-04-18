package com.rankstream.backend.domain.auditor

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class TimestampEntityListener {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.MIN

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.MIN

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    lateinit var createdBy: String

    @LastModifiedBy
    @Column(name = "updated_by", nullable = false)
    lateinit var updatedBy: String

}
