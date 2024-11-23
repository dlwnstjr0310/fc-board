package com.study.fcboard.controller.dto.response

import com.study.fcboard.service.dto.response.CommentResponseDTO

data class CommentResponse(
    val id: Long,
    val content: String,
    val createdBy: String,
    val createdAt: String,
)

fun CommentResponseDTO.toResponse() = CommentResponse(
    id = this.id,
    content = this.content,
    createdBy = this.createdBy,
    createdAt = this.createdAt
)
