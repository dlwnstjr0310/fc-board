package com.study.fcboard.controller

import com.study.fcboard.controller.dto.PostCreateRequest
import com.study.fcboard.controller.dto.PostDetailResponse
import com.study.fcboard.controller.dto.PostSearchRequest
import com.study.fcboard.controller.dto.PostSummaryResponse
import com.study.fcboard.controller.dto.PostUpdateRequest
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
import java.time.LocalDateTime

@RestController
@RequestMapping("/posts")
class PostController {

    @PostMapping
    fun createPost(
        @RequestBody request: PostCreateRequest,
    ): Long {
        return 1L
    }

    @PutMapping("/{id}")
    fun updatePost(
        @PathVariable id: Long,
        @RequestBody request: PostUpdateRequest,
    ): Long {
        return id
    }

    @DeleteMapping("/{id}")
    fun deletePost(
        @PathVariable id: Long,
        @RequestParam createdBy: String,
    ): Long {
        println(createdBy)
        return id
    }

    @GetMapping("/{id}")
    fun getPost(
        @PathVariable id: Long,
    ): PostDetailResponse {
        return PostDetailResponse(
            1L,
            "title",
            "content",
            "createdBy",
            LocalDateTime.now().toString()
        )
    }

    @GetMapping
    fun getPosts(
        pageable: Pageable,
        postSearchRequest: PostSearchRequest,
    ): Page<PostSummaryResponse> {
        return Page.empty()
    }
}
