package com.study.fcboard.repository

import com.study.fcboard.domain.Like
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository : JpaRepository<Like, Long>
