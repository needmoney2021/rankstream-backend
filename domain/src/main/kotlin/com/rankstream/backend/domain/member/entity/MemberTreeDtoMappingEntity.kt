package com.rankstream.backend.domain.member.entity

import com.rankstream.backend.domain.member.dto.tree.MemberTreeDto
import jakarta.persistence.ColumnResult
import jakarta.persistence.ConstructorResult
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.SqlResultSetMapping

@SqlResultSetMapping(
    name = "MemberTreeDtoMapping",
    classes = [
        ConstructorResult(
            targetClass = MemberTreeDto::class,
            columns = [
                ColumnResult(name = "idx", type = Long::class),
                ColumnResult(name = "member_id", type = String::class),
                ColumnResult(name = "member_name", type = String::class),
                ColumnResult(name = "position", type = String::class),
                ColumnResult(name = "grade_idx", type = Long::class),
                ColumnResult(name = "depth", type = Int::class),
            ]
        )
    ]
)
@MappedSuperclass
class MemberTreeDtoMappingEntity // dummy class just to attach the mapping
