package com.study.fcboard.controller.dto.request

import com.study.fcboard.service.dto.request.PostSearchRequestDTO
import org.springframework.web.bind.annotation.RequestParam

data class PostSearchRequest(
    @RequestParam
    val title: String?,
    @RequestParam
    val createdBy: String?,
    @RequestParam
    val tag: String?,
)

fun PostSearchRequest.toDTO() = PostSearchRequestDTO(
    title = title,
    createdBy = createdBy,
    tag = tag
)
