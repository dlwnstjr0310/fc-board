package com.study.fcboard.controller.dto.request

import com.study.fcboard.service.dto.request.CommentCreateRequestDTO

data class CommentCreateRequest(
    val content: String,
    val createdBy: String,
)

fun CommentCreateRequest.toDTO() = CommentCreateRequestDTO(
    content = content,
    createdBy = createdBy
)
