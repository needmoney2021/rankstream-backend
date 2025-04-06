package com.rankstream.backend.domain.company.code.entity

import com.rankstream.backend.domain.auditor.TimestampEntityListener
import com.rankstream.backend.domain.company.code.embeddable.CodeId
import com.rankstream.backend.domain.company.code.enums.CodeType
import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "common_code")
class Code(

    @Id
    @EmbeddedId
    val codeId: CodeId,

    @Column(name = "code_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    val codeType: CodeType,

    @Column(name = "code_value", nullable = false, length = 20)
    val codeValue: String
) : TimestampEntityListener() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Code) return false

        if (codeId != other.codeId) return false

        return true
    }

    override fun hashCode(): Int {
        return codeId.hashCode()
    }

    override fun toString(): String {
        return "Code(codeId=$codeId, codeType=$codeType, codeValue='$codeValue')"
    }

}
