package com.rankstream.backend.domain.company.enums

enum class CommissionPlan {
    BINARY,
    GENERAL;

    companion object {
        // 추후 트리는 이진트리형식이지만 이름과 정책이 다른 플랜이 추가될 가능성을 고려.
        fun isBinary(plan: CommissionPlan): Boolean {
            return plan == BINARY
        }
    }
}
