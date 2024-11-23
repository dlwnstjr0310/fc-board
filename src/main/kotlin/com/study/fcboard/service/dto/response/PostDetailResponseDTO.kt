package com.study.fcboard.service.dto.response

import com.study.fcboard.domain.Post

data class PostDetailResponseDTO(
    val id: Long,
    val title: String,
    val content: String,
    val createdBy: String,
    val createdAt: String,
    val comments: List<CommentResponseDTO>,
)

fun Post.toDetailResponseDTO() = PostDetailResponseDTO(
    id = id,
    title = title,
    content = content,
    createdBy = createdBy,
    createdAt = createdAt.toString(),
    comments = comments.map { it.toResponseDTO() }
)