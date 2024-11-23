package com.study.fcboard.service

import com.study.fcboard.domain.Post
import com.study.fcboard.exception.PostNotDeletableException
import com.study.fcboard.exception.PostNotFoundException
import com.study.fcboard.exception.PostNotUpdatableException
import com.study.fcboard.repository.PostRepository
import com.study.fcboard.service.dto.PostCreateRequestDTO
import com.study.fcboard.service.dto.PostSearchRequestDTO
import com.study.fcboard.service.dto.PostUpdateRequestDTO
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
) : BehaviorSpec({

    beforeSpec {
        postRepository.saveAll(
            listOf(
                Post("title1", "content1", "junstone1"),
                Post("title12", "content1", "junstone1"),
                Post("title13", "content1", "junstone1"),
                Post("title14", "content1", "junstone1"),
                Post("title15", "content1", "junstone1"),
                Post("title6", "content1", "junstone2"),
                Post("title7", "content1", "junstone2"),
                Post("title8", "content1", "junstone2"),
                Post("title9", "content1", "junstone2"),
                Post("title10", "content1", "junstone2")
            )
        )
    }

    given("게시글 생성 시 ") {
        When("게시글 인풋이 정상적으로 들어오면") {

            val postId = postService.createPost(
                PostCreateRequestDTO(
                    title = "제목",
                    content = "내용",
                    createdBy = "junstone"
                )
            )

            then("게시글이 정상적으로 생성됨을 확인한다.") {
                postId shouldBeGreaterThan 0L

                val post = postRepository.findByIdOrNull(postId)
                post shouldNotBe null
                post?.title shouldBe "제목"
                post?.content shouldBe "내용"
                post?.createdBy shouldBe "junstone"
            }
        }
    }

    given("게시글 수정 시") {

        val saved = postRepository.save(
            Post(
                "title",
                "content",
                "junstone"
            )
        )

        When("정상 수정 시") {

            val updatedId = postService.updatePost(
                saved.id,
                PostUpdateRequestDTO(
                    "update title",
                    "update content",
                    "junstone"
                )
            )

            then("게시글이 정상적으로 수정됨을 확인한다.") {

                saved.id shouldBe updatedId
                val updated = postRepository.findByIdOrNull(updatedId)
                updated shouldNotBe null
                updated?.title shouldBe "update title"
                updated?.content shouldBe "update content"
                updated?.updatedBy shouldBe "junstone"
            }
        }

        When("게시글이 없을 때") {

            then("게시글을 찾을 수 없다는 예외가 발생한다.") {
                shouldThrow<PostNotFoundException> {
                    postService.updatePost(
                        9999L,
                        PostUpdateRequestDTO(
                            "update title",
                            "update content",
                            "update junstone"
                        )
                    )
                }
            }
        }

        When("작성자가 동일하지 않으면") {

            then("수정할 수 없는 게시물이라는 예외가 발생한다.") {
                shouldThrow<PostNotUpdatableException> {
                    postService.updatePost(
                        1L,
                        PostUpdateRequestDTO(
                            "update title",
                            "update content",
                            "update junstone"
                        )
                    )
                }
            }
        }
    }

    given("게시글 삭제 시") {

        val saved = postRepository.save(
            Post(
                "title",
                "content",
                "junstone"
            )
        )

        When("정상 삭제 시") {

            val postId = postService.deletePost(saved.id, "junstone")

            then("게시글이 정상적으로 삭제됨을 확인한다.") {

                postId shouldBe saved.id
                postRepository.findByIdOrNull(postId) shouldBe null
            }
        }

        When("작성자가 동일하지 않으면") {

            then("삭제할 수 없는 게시물입니다 라는 예외가 발생한다.") {

                val saved2 = postRepository.save(
                    Post(
                        "title",
                        "content",
                        "junstone"
                    )
                )

                shouldThrow<PostNotDeletableException> {
                    postService.deletePost(saved2.id, "junstone@")
                }
            }
        }
    }

    given("게시글 상세조회 시") {

        val saved = postRepository.save(
            Post(
                "title",
                "content",
                "junstone"
            )
        )

        When("정상 조회 시") {

            val post = postService.getPost(saved.id)

            then("게시글의 내용이 정상적으로 반환됨을 확인한다.") {

                post.id shouldBe saved.id
                post.title shouldBe "title"
                post.content shouldBe "content"
                post.createdBy shouldBe "junstone"
            }
        }

        When("게시글이 없을 때") {

            then("게시글을 찾을 수 없다라는 예외가 발생한다.") {

                shouldThrow<PostNotFoundException> {
                    postService.getPost(999L)
                }
            }
        }
    }

    given("게시글 목록 조회 시") {

        When("정상 조회 시") {

            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDTO())

            then("게시글 페이지가 반환된다.") {

                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title"
                postPage.content[0].createdBy shouldContain "junstone"
            }
        }

        When("타이틀로 검색") {

            then("타이틀에 해당하는 게시글이 반환된다.") {

                val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDTO(title = "title1"))

                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title1"
                postPage.content[0].createdBy shouldContain "junstone"
            }
        }

        When("작성자로 검색") {

            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDTO(createdBy = "junstone1"))

            then("작성자에 해당하는 게시글이 반환된다.") {

                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title"
                postPage.content[0].createdBy shouldBe "junstone1"
            }
        }
    }
})
