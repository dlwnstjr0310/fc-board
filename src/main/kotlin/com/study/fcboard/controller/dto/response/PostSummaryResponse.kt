package com.study.fcboard.controller.dto.response

import com.study.fcboard.service.dto.response.PostSummaryResponseDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

data class PostSummaryResponse(

    val id: Long,
    val title: String,
    val createdBy: String,
    val createdAt: String,
    val tag: String? = null,

)

fun Page<PostSummaryResponseDTO>.toResponse() = PageImpl(
    this.content.map { it.toResponse() },
    this.pageable,
    this.totalElements
)

fun PostSummaryResponseDTO.toResponse() = PostSummaryResponse(
    id = this.id,
    title = this.title,
    createdBy = this.createdBy,
    createdAt = this.createdAt
)
