package com.rankstream.backend.domain.company.code.embeddable

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class CodeId(

    @Column(name = "super_code", length = 3, nullable = false)
    val superCode: String,

    @Column(name = "sub_code", length = 3, nullable = false)
    val subCode: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CodeId) return false

        if (superCode != other.superCode) return false
        if (subCode != other.subCode) return false

        return true
    }

    override fun hashCode(): Int {
        var result = superCode.hashCode()
        result = 31 * result + subCode.hashCode()
        return result
    }

}
