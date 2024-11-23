package com.study.fcboard.controller.dto.request

import com.study.fcboard.service.dto.request.CommentUpdateRequestDTO

data class CommentUpdateRequest(
    val content: String,
    val updatedBy: String,
)

fun CommentUpdateRequest.toDTO() = CommentUpdateRequestDTO(
    content = content,
    updatedBy = updatedBy
)
