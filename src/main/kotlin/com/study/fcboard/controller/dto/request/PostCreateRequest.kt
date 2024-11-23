package com.study.fcboard.controller.dto.request

import com.study.fcboard.service.dto.request.PostCreateRequestDTO

data class PostCreateRequest(
    val title: String,
    val content: String,
    val createdBy: String,
    val tags: List<String> = emptyList(),
)

fun PostCreateRequest.toDTO() = PostCreateRequestDTO(
    title = title,
    content = content,
    createdBy = createdBy,
    tags = tags
)
