package com.study.fcboard.service.dto.request

import com.study.fcboard.domain.Comment
import com.study.fcboard.domain.Post

data class CommentCreateRequestDTO(
    val content: String,
    val createdBy: String,
)

fun CommentCreateRequestDTO.toEntity(post: Post): Comment = Comment(
    content = content,
    createdBy = createdBy,
    post = post
)
