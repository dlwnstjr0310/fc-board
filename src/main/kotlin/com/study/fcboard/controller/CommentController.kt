package com.study.fcboard.controller

import com.study.fcboard.controller.dto.CommentCreateRequest
import com.study.fcboard.controller.dto.CommentUpdateRequest
import com.study.fcboard.controller.dto.toDTO
import com.study.fcboard.service.CommentService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CommentController(
    private val commentService: CommentService,
) {

    @PostMapping("/posts/{postId}/comments")
    fun createComment(
        @PathVariable postId: Long,
        @RequestBody commentCreateRequest: CommentCreateRequest,
    ): Long =
        commentService.createComment(postId, commentCreateRequest.toDTO())

    @PutMapping("/comments/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody commentUpdateRequest: CommentUpdateRequest,
    ): Long =
        commentService.updateComment(commentId, commentUpdateRequest.toDTO())

    @DeleteMapping("/comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @RequestParam deletedBy: String,
    ): Long =
        commentService.deleteComment(commentId, deletedBy)
}
