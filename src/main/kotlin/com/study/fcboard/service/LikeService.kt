package com.study.fcboard.service

import com.study.fcboard.domain.Like
import com.study.fcboard.exception.PostNotFoundException
import com.study.fcboard.repository.LikeRepository
import com.study.fcboard.repository.PostRepository
import com.study.fcboard.util.RedisUtil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LikeService(
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
    private val redisUtil: RedisUtil,
) {

    @Transactional
    fun createLike(postId: Long, createdBy: String): Long {

        redisUtil.increment(redisUtil.getLikeCountKey(postId))

        return likeRepository.save(
            Like(
                postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException(),
                createdBy
            )
        ).id
    }

    fun countLike(postId: Long): Long {

        redisUtil.getCount(redisUtil.getLikeCountKey(postId))?.let {
            return it
        }

        with(likeRepository.countByPostId(postId)) {
            redisUtil.setData(redisUtil.getLikeCountKey(postId), this)
            return this
        }
    }
}
