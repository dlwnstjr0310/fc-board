package com.study.fcboard.service

import com.study.fcboard.exception.PostNotDeletableException
import com.study.fcboard.exception.PostNotFoundException
import com.study.fcboard.repository.PostRepository
import com.study.fcboard.service.dto.PostCreateRequestDTO
import com.study.fcboard.service.dto.PostDetailResponseDTO
import com.study.fcboard.service.dto.PostSearchRequestDTO
import com.study.fcboard.service.dto.PostSummaryResponseDTO
import com.study.fcboard.service.dto.PostUpdateRequestDTO
import com.study.fcboard.service.dto.toDetailResponseDTO
import com.study.fcboard.service.dto.toEntity
import com.study.fcboard.service.dto.toSummaryResponseDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(private val postRepository: PostRepository) {

    @Transactional
    fun createPost(requestDTO: PostCreateRequestDTO): Long =
        postRepository.save(requestDTO.toEntity()).id

    @Transactional
    fun updatePost(id: Long, requestDTO: PostUpdateRequestDTO): Long {
        val post = postRepository.findByIdOrNull(id) ?: throw PostNotFoundException()

        post.update(requestDTO)

        return id
    }

    @Transactional
    fun deletePost(id: Long, deletedBy: String): Long {
        val post = postRepository.findByIdOrNull(id) ?: throw PostNotFoundException()

        if (post.createdBy != deletedBy) {
            throw PostNotDeletableException()
        }

        postRepository.delete(post)

        return id
    }

    fun getPost(id: Long): PostDetailResponseDTO {
        return postRepository.findByIdOrNull(id)
            ?.toDetailResponseDTO()
            ?: throw PostNotFoundException()
    }

    fun findPageBy(pageRequest: Pageable, postSearchRequestDTO: PostSearchRequestDTO): Page<PostSummaryResponseDTO> =
        postRepository.findPageBy(pageRequest, postSearchRequestDTO).toSummaryResponseDTO()
}