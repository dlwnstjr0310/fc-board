package com.study.fcboard.service.dto.response

import com.study.fcboard.domain.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

data class PostSummaryResponseDTO(
    val id: Long,
    val title: String,
    val createdBy: String,
    val createdAt: String,
    val firstTag: String?,
)

fun Page<Post>.toSummaryResponseDTO() = PageImpl(
    content.map { it.toSummaryResponseDTO() },
    pageable,
    totalElements
)

fun Post.toSummaryResponseDTO() = PostSummaryResponseDTO(
    id = id,
    title = title,
    createdBy = createdBy,
    createdAt = createdAt.toString(),
    firstTag = tags.firstOrNull()?.name
)
