package com.study.fcboard.controller.dto.response

import com.study.fcboard.service.dto.response.PostDetailResponseDTO

data class PostDetailResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdBy: String,
    val createdAt: String,
    val comments: List<CommentResponse> = emptyList(),
    val tags: List<String> = emptyList(),
)

fun PostDetailResponseDTO.toResponse() = PostDetailResponse(
    id = this.id,
    title = this.title,
    content = this.content,
    createdBy = this.createdBy,
    createdAt = this.createdAt,
    comments = this.comments.map { it.toResponse() }
)
