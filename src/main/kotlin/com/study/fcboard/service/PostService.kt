package com.study.fcboard.service

import com.study.fcboard.exception.PostNotDeletableException
import com.study.fcboard.exception.PostNotFoundException
import com.study.fcboard.repository.PostRepository
import com.study.fcboard.service.dto.request.PostCreateRequestDTO
import com.study.fcboard.service.dto.request.PostSearchRequestDTO
import com.study.fcboard.service.dto.request.PostUpdateRequestDTO
import com.study.fcboard.service.dto.request.toEntity
import com.study.fcboard.service.dto.response.PostDetailResponseDTO
import com.study.fcboard.service.dto.response.PostSummaryResponseDTO
import com.study.fcboard.service.dto.response.toDetailResponseDTO
import com.study.fcboard.service.dto.response.toSummaryResponseDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
    private val likeService: LikeService,
) {

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
            ?.toDetailResponseDTO(likeService.countLike(id))
            ?: throw PostNotFoundException()
    }

    fun findPageBy(pageRequest: Pageable, postSearchRequestDTO: PostSearchRequestDTO): Page<PostSummaryResponseDTO> =
        postRepository.findPageBy(pageRequest, postSearchRequestDTO)
            .toSummaryResponseDTO(likeService::countLike)
}
