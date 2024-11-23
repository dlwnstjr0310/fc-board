package com.study.fcboard.service.dto.request

import com.study.fcboard.domain.Post

data class PostCreateRequestDTO(
    val title: String,
    val content: String,
    val createdBy: String,
    val tags: List<String> = emptyList(),
)

fun PostCreateRequestDTO.toEntity() = Post(
    title = title,
    content = content,
    createdBy = createdBy,
    tags = tags
)
