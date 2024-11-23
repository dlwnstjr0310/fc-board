package com.study.fcboard.domain

import com.study.fcboard.exception.CommentNotUpdatableException
import com.study.fcboard.service.dto.CommentUpdateRequestDTO
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class Comment(
    content: String,
    post: Post,
    createdBy: String,
) : BaseEntity(createdBy = createdBy) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    var content: String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    var post: Post = post
        protected set

    fun update(updateRequestDTO: CommentUpdateRequestDTO) {
        if (updateRequestDTO.updatedBy != this.createdBy) {
            throw CommentNotUpdatableException()
        }
        this.content = updateRequestDTO.content
        super.updatedBy(updateRequestDTO.updatedBy)
    }
}
