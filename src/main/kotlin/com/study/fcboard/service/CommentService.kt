package com.study.fcboard.service

import com.study.fcboard.exception.CommentNotDeletableException
import com.study.fcboard.exception.CommentNotFoundException
import com.study.fcboard.exception.PostNotFoundException
import com.study.fcboard.repository.CommentRepository
import com.study.fcboard.repository.PostRepository
import com.study.fcboard.service.dto.CommentCreateRequestDTO
import com.study.fcboard.service.dto.CommentUpdateRequestDTO
import com.study.fcboard.service.dto.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) {

    @Transactional
    fun createComment(postId: Long, createRequestDTO: CommentCreateRequestDTO): Long {
        val post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()

        return commentRepository.save(
            createRequestDTO.toEntity(post)
        ).id
    }

    @Transactional
    fun updateComment(id: Long, updateRequestDTO: CommentUpdateRequestDTO): Long {
        val comment = commentRepository.findByIdOrNull(id) ?: throw CommentNotFoundException()
        comment.update(updateRequestDTO)

        return comment.id
    }

    @Transactional
    fun deleteComment(id: Long, deletedBy: String): Long {
        val comment = commentRepository.findByIdOrNull(id) ?: throw CommentNotFoundException()

        if (comment.createdBy != deletedBy) {
            throw CommentNotDeletableException()
        }

        commentRepository.delete(comment)

        return id
    }
}
