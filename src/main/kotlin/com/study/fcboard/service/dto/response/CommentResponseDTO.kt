package com.study.fcboard.service.dto.response

import com.study.fcboard.domain.Comment

data class CommentResponseDTO(
    val id: Long,
    val content: String,
    val createdBy: String,
    val createdAt: String,
)

fun Comment.toResponseDTO() = CommentResponseDTO(
    id = id,
    content = content,
    createdBy = createdBy,
    createdAt = createdAt.toString(),
)
