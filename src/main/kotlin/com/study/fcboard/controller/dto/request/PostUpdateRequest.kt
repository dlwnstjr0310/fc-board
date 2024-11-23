package com.study.fcboard.controller.dto.request

import com.study.fcboard.service.dto.request.PostUpdateRequestDTO

data class PostUpdateRequest(
    val title: String,
    val content: String,
    val updatedBy: String,
)

fun PostUpdateRequest.toDTO() = PostUpdateRequestDTO(
    title = title,
    content = content,
    updatedBy = updatedBy
)
