package com.rankstream.backend.domain.grade.dto.response

import com.rankstream.backend.domain.grade.entity.Grade
import java.time.LocalDateTime

data class GradeResponse(
    val idx: Long,
    val name: String,
    val code: String,
    val achievementPoint: Double,
    val refundRate: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val createdBy: String,
    val updatedBy: String
) {
    companion object {
        fun fromEntity(grade: Grade): GradeResponse {
            return GradeResponse(
                idx = grade.idx!!,
                name = grade.gradeName,
                code = grade.gradeCode,
                achievementPoint = grade.requiredPoint,
                refundRate = grade.paybackRatio,
                createdAt = grade.createdAt,
                updatedAt = grade.updatedAt,
                createdBy = grade.createdBy,
                updatedBy = grade.updatedBy
            )
        }
    }
}
