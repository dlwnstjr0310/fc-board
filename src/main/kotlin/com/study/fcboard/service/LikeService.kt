package com.study.fcboard.service

import com.study.fcboard.repository.LikeRepository
import org.springframework.stereotype.Service

@Service
class LikeService(
    private val likeRepository: LikeRepository,
)
