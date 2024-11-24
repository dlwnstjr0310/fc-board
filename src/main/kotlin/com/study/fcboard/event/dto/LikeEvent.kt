package com.study.fcboard.event.dto

data class LikeEvent(
    val postId: Long,
    val createdBy: String,
)
