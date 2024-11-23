package com.study.fcboard.service.dto

import com.study.fcboard.domain.Post

data class PostDetailResponseDTO(
    val id: Long,
    val title: String,
    val content: String,
    val createdBy: String,
    val createdAt: String,
)

fun Post.toDetailResponseDTO() = PostDetailResponseDTO(
    id = id,
    title = title,
    content = content,
    createdBy = createdBy,
    createdAt = createdAt.toString()
)
