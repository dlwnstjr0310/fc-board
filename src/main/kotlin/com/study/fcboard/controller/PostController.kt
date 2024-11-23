package com.study.fcboard.controller

import com.study.fcboard.controller.dto.request.PostCreateRequest
import com.study.fcboard.controller.dto.response.PostDetailResponse
import com.study.fcboard.controller.dto.request.PostSearchRequest
import com.study.fcboard.controller.dto.response.PostSummaryResponse
import com.study.fcboard.controller.dto.request.PostUpdateRequest
import com.study.fcboard.controller.dto.request.toDTO
import com.study.fcboard.controller.dto.response.toResponse
import com.study.fcboard.service.PostService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService,
) {

    @PostMapping
    fun createPost(
        @RequestBody request: PostCreateRequest,
    ): Long =
        postService.createPost(request.toDTO())

    @PutMapping("/{id}")
    fun updatePost(
        @PathVariable id: Long,
        @RequestBody request: PostUpdateRequest,
    ): Long =
        postService.updatePost(id, request.toDTO())

    @DeleteMapping("/{id}")
    fun deletePost(
        @PathVariable id: Long,
        @RequestParam createdBy: String,
    ): Long =
        postService.deletePost(id, createdBy)

    @GetMapping("/{id}")
    fun getPost(
        @PathVariable id: Long,
    ): PostDetailResponse =
        postService.getPost(id).toResponse()

    @GetMapping
    fun getPosts(
        pageable: Pageable,
        postSearchRequest: PostSearchRequest,
    ): Page<PostSummaryResponse> =
        postService.findPageBy(pageable, postSearchRequest.toDTO()).toResponse()
}
