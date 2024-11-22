package com.study.fcboard.service.dto

data class PostUpdateRequestDTO(
    val title: String,
    val content: String,
    val updatedBy: String,
)
