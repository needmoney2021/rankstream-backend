package com.rankstream.backend.domain.auth.entity

import com.rankstream.backend.domain.auditor.TimestampEntityListener
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.hibernate.proxy.HibernateProxy

@Entity
@Table(
    name = "member_auth",
    indexes = [
        Index(name = "UIDX_AUTH_REFRESH_TOKEN", columnList = "refresh_token", unique = true),
    ]
)
class RefreshToken(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null,

    @Column(length = 500, nullable = false)
    val refreshToken: String

) : TimestampEntityListener() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false

        val otherRefreshToken = when (other) {
            is HibernateProxy -> (other.hibernateLazyInitializer.implementation as? RefreshToken)
            is RefreshToken -> other
            else -> return false
        }


        if (idx != otherRefreshToken?.idx) return false
        if (refreshToken != otherRefreshToken?.refreshToken) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idx?.hashCode() ?: 0
        result = 31 * result + refreshToken.hashCode()
        return result
    }
}
