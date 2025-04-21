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
    val children: MutableMap<MemberPosition, BinaryTreeNode> = mutableMapOf()
) : TreeNode()

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
