package com.study.fcboard.controller.dto

import com.study.fcboard.service.dto.PostSearchRequestDTO
import org.springframework.web.bind.annotation.RequestParam

data class PostSearchRequest(
    @RequestParam
    val title: String?,
    @RequestParam
    val createdBy: String?,
)

fun PostSearchRequest.toDTO() = PostSearchRequestDTO(
    title = title,
    createdBy = createdBy
)
