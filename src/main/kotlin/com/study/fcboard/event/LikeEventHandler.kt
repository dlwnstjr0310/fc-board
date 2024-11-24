package com.study.fcboard.event

import com.study.fcboard.domain.Like
import com.study.fcboard.event.dto.LikeEvent
import com.study.fcboard.exception.PostNotFoundException
import com.study.fcboard.repository.LikeRepository
import com.study.fcboard.repository.PostRepository
import com.study.fcboard.util.RedisUtil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener

@Service
class LikeEventHandler(
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
    private val redisUtil: RedisUtil,
) {

    @Async
    @TransactionalEventListener(LikeEvent::class)
    fun handle(event: LikeEvent) {
        Thread.sleep(3000)
        redisUtil.increment(redisUtil.getLikeCountKey(event.postId))

        likeRepository.save(
            Like(
                postRepository.findByIdOrNull(event.postId) ?: throw PostNotFoundException(),
                event.createdBy
            )
        ).id
    }
}
