package com.study.fcboard.controller.dto

import com.study.fcboard.service.dto.PostCreateRequestDTO

data class PostCreateRequest(
    val title: String,
    val content: String,
    val createdBy: String,
)

fun PostCreateRequest.toDTO() = PostCreateRequestDTO(
    title = title,
    content = content,
    createdBy = createdBy,
)
