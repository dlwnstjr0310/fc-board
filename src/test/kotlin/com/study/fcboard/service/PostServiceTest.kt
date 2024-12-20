package com.study.fcboard.service

import com.study.fcboard.domain.Comment
import com.study.fcboard.domain.Post
import com.study.fcboard.domain.Tag
import com.study.fcboard.exception.PostNotDeletableException
import com.study.fcboard.exception.PostNotFoundException
import com.study.fcboard.exception.PostNotUpdatableException
import com.study.fcboard.repository.CommentRepository
import com.study.fcboard.repository.PostRepository
import com.study.fcboard.repository.TagRepository
import com.study.fcboard.service.dto.request.PostCreateRequestDTO
import com.study.fcboard.service.dto.request.PostSearchRequestDTO
import com.study.fcboard.service.dto.request.PostUpdateRequestDTO
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.testcontainers.perSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.testcontainers.containers.GenericContainer

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val tagRepository: TagRepository,
    private val likeService: LikeService,
) : BehaviorSpec({

    val redisContainer = GenericContainer<Nothing>("redis:latest")

    afterSpec {
        redisContainer.stop()
    }

    beforeSpec {

        redisContainer.portBindings.add("16379:6379")
        redisContainer.start()
        listener(redisContainer.perSpec())

        postRepository.saveAll(
            listOf(
                Post("title1", "content1", "junstone1", tags = listOf("tag1", "tag2")),
                Post("title12", "content1", "junstone1", tags = listOf("tag1", "tag2")),
                Post("title13", "content1", "junstone1", tags = listOf("tag1", "tag2")),
                Post("title14", "content1", "junstone1", tags = listOf("tag1", "tag2")),
                Post("title15", "content1", "junstone1", tags = listOf("tag1", "tag2")),
                Post("title6", "content1", "junstone2", tags = listOf("tag1", "tag5")),
                Post("title7", "content1", "junstone2", tags = listOf("tag1", "tag5")),
                Post("title8", "content1", "junstone2", tags = listOf("tag1", "tag5")),
                Post("title9", "content1", "junstone2", tags = listOf("tag1", "tag5")),
                Post("title10", "content1", "junstone2", tags = listOf("tag1", "tag5"))
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

        When("태그가 추가되면") {

            val postId = postService.createPost(
                PostCreateRequestDTO(
                    title = "제목",
                    content = "내용",
                    createdBy = "junstone",
                    tags = listOf("tag1", "tag2")
                )
            )

            then("태그가 정상적으로 추가됨을 확인한다.") {
                val tags = tagRepository.findByPostId(postId)

                tags.size shouldBe 2
                tags[0].name shouldBe "tag1"
                tags[1].name shouldBe "tag2"
            }
        }
    }

    given("게시글 수정 시") {

        val saved = postRepository.save(
            Post(
                "title",
                "content",
                "junstone",
                tags = listOf("tag1", "tag2")
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

        When("태그가 수정되었을 때") {

            val updatedId = postService.updatePost(
                saved.id,
                PostUpdateRequestDTO(
                    "update title",
                    "update content",
                    "junstone",
                    listOf("tag1", "tag2", "tag3")
                )
            )

            then("정상적으로 수정됨을 확인한다.") {

                val tags = tagRepository.findByPostId(updatedId)

                tags.size shouldBe 3
                tags[2].name shouldBe "tag3"
            }

            then("태그 순서가 변경되었을 때 정상적으로 변경됨을 확인한다.") {

                postService.updatePost(
                    saved.id,
                    PostUpdateRequestDTO(
                        "update title",
                        "update content",
                        "junstone",
                        listOf("tag3", "tag2", "tag1")
                    )
                )

                val tags = tagRepository.findByPostId(updatedId)

                tags.size shouldBe 3
                tags[2].name shouldBe "tag1"
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

        tagRepository.saveAll(
            listOf(
                Tag("tag1", saved, "junstone"),
                Tag("tag2", saved, "junstone"),
                Tag("tag3", saved, "junstone")
            )
        )

        likeService.createLike(saved.id, "junstone")
        likeService.createLike(saved.id, "junstone1")
        likeService.createLike(saved.id, "junstone2")
        When("정상 조회 시") {

            val post = postService.getPost(saved.id)

            then("게시글의 내용이 정상적으로 반환됨을 확인한다.") {

                post.id shouldBe saved.id
                post.title shouldBe "title"
                post.content shouldBe "content"
                post.createdBy shouldBe "junstone"
            }

            then("태그가 정상적으로 조회됨을 확인한다.") {

                post.tags.size shouldBe 3
                post.tags[0] shouldBe "tag1"
                post.tags[1] shouldBe "tag2"
                post.tags[2] shouldBe "tag3"
            }

            then("좋아요 개수가 조회됨을 확인한다.") {
                post.likeCount shouldBe 3
            }
        }

        When("게시글이 없을 때") {

            then("게시글을 찾을 수 없다라는 예외가 발생한다.") {

                shouldThrow<PostNotFoundException> {
                    postService.getPost(999L)
                }
            }
        }

        When("댓글 추가 시") {

            commentRepository.saveAll(
                listOf(
                    Comment("댓글 내용1", saved, "댓글 작성자"),
                    Comment("댓글 내용2", saved, "댓글 작성자"),
                    Comment("댓글 내용3", saved, "댓글 작성자")
                )
            )

            val post = postService.getPost(saved.id)

            then("댓글이 함께 조회됨을 확인한다.") {

                post.comments.size shouldBe 3
                post.comments[0].content shouldBe "댓글 내용1"
                post.comments[1].content shouldBe "댓글 내용2"
                post.comments[2].content shouldBe "댓글 내용3"
                post.comments[0].createdBy shouldBe "댓글 작성자"
                post.comments[1].createdBy shouldBe "댓글 작성자"
                post.comments[2].createdBy shouldBe "댓글 작성자"
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

            then("첫번째 태그가 함께 조회됨을 확인한다.") {

                postPage.content.forEach {
                    it.firstTag shouldBe "tag1"
                }
            }
        }

        When("태그로 검색") {

            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDTO(tag = "tag5"))

            then("태그에 해당하는 게시글이 반환된다.") {

                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldBe "title6"
                postPage.content[1].title shouldBe "title7"
                postPage.content[2].title shouldBe "title8"
                postPage.content[3].title shouldBe "title9"
                postPage.content[4].title shouldBe "title10"
            }
        }

        When("좋아요가 2개 추가되었을 때") {

            val postPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDTO(tag = "tag5"))
            postPage.content.forEach {
                likeService.createLike(it.id, "junstone1")
                likeService.createLike(it.id, "junstone2")
            }

            val likedPostPage = postService.findPageBy(PageRequest.of(0, 5), PostSearchRequestDTO(tag = "tag5"))

            then("좋아요 개수가 정상적으로 조회됨을 확인한다.") {

                likedPostPage.content.forEach {
                    it.likeCount shouldBe 2
                }
            }
        }
    }
})
