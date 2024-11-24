package com.study.fcboard.service

import com.study.fcboard.domain.Like
import com.study.fcboard.exception.PostNotFoundException
import com.study.fcboard.repository.LikeRepository
import com.study.fcboard.repository.PostRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LikeService(
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
) {

    @Transactional
    fun createLike(postId: Long, createdBy: String): Long =
        likeRepository.save(
            Like(
                postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException(),
                createdBy
            )
        ).id

    fun countLike(postId: Long) =
        likeRepository.countByPostId(postId)
}
