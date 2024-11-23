package com.study.fcboard.controller.dto

import com.study.fcboard.service.dto.CommentCreateRequestDTO

data class CommentCreateRequest(
    val content: String,
    val createdBy: String,
)

fun CommentCreateRequest.toDTO() = CommentCreateRequestDTO(
    content = content,
    createdBy = createdBy
)
