package com.study.fcboard.controller.dto

import com.study.fcboard.service.dto.CommentUpdateRequestDTO

data class CommentUpdateRequest(
    val content: String,
    val updatedBy: String,
)

fun CommentUpdateRequest.toDTO() = CommentUpdateRequestDTO(
    content = content,
    updatedBy = updatedBy
)
