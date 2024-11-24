package com.study.fcboard.service

import com.study.fcboard.domain.Post
import com.study.fcboard.exception.PostNotFoundException
import com.study.fcboard.repository.LikeRepository
import com.study.fcboard.repository.PostRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.testcontainers.perSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.testcontainers.containers.GenericContainer

@SpringBootTest
class LikeServiceTest(
    private val likeService: LikeService,
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
) : BehaviorSpec({

    val redisContainer = GenericContainer<Nothing>("redis:latest")

    beforeSpec {
        redisContainer.portBindings.add("16379:6379")
        redisContainer.start()
        listener(redisContainer.perSpec())
    }

    afterSpec {
        redisContainer.stop()
    }

    given("좋아요 생성 시") {

        val saved = postRepository.save(
            Post("title", "content", "junstone")
        )

        When("인풋이 정상적으로 들어오면") {

            val likeId = likeService.createLike(1L, "junstone")

            then("좋아요가 정상적으로 생성됨을 확인한다.") {

                val like = likeRepository.findByIdOrNull(likeId)

                like shouldNotBe null
                like?.createdBy shouldBe "junstone"
            }
        }

        When("게시글이 존재하지 않으면") {

            then("존재하지 않는 게시글 예외가 발생한다.") {

                shouldThrow<PostNotFoundException> {
                    likeService.createLike(999L, "junstone")
                }
            }
        }
    }
})
