package com.study.fcboard.service.dto.response

import com.study.fcboard.domain.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

fun Page<Tag>.toSummaryResponseDTO(countLike: (Long) -> Long) = PageImpl(
    content.map { it.toSummaryResponseDTO(countLike) },
    pageable,
    totalElements
)

fun Tag.toSummaryResponseDTO(countLike: (Long) -> Long) = PostSummaryResponseDTO(
    id = post.id,
    title = post.title,
    createdBy = post.createdBy,
    createdAt = post.createdAt.toString(),
    firstTag = name,
    likeCount = countLike(post.id)
)
