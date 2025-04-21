package com.rankstream.backend.domain.member.dto.response

import com.rankstream.backend.domain.member.enums.MemberPosition

sealed class TreeNode {
    abstract val idx: Long
    abstract val id: String
    abstract val name: String
    abstract val depth: Int
    abstract val gradeLevel: Int
    val x = 0
    val y = 0
    val radius = 0
}

data class BinaryTreeNode(
    override val idx: Long,
    override val id: String,
    override val name: String,
    override val depth: Int,
    override val gradeLevel: Int,
    val children: Array<BinaryTreeNode?> = arrayOfNulls(2)
) : TreeNode() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BinaryTreeNode) return false

        if (idx != other.idx) return false
        if (depth != other.depth) return false
        if (gradeLevel != other.gradeLevel) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (!children.contentEquals(other.children)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idx.hashCode()
        result = 31 * result + depth
        result = 31 * result + gradeLevel
        result = 31 * result + id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + children.contentHashCode()
        return result
    }
}

data class GeneralTreeNode(
    override val idx: Long,
    override val id: String,
    override val name: String,
    override val depth: Int,
    override val gradeLevel: Int,
    val children: MutableList<GeneralTreeNode> = mutableListOf()
) : TreeNode()

data class TreeLink(
    val source: Long,
    val target: Long
)

data class MemberTreeResponse(
    val nodes: List<TreeNode>,
    val links: List<TreeLink>,
    val isBinary: Boolean
)
