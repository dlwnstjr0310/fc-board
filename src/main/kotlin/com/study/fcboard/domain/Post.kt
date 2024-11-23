package com.study.fcboard.domain

import com.study.fcboard.exception.PostNotUpdatableException
import com.study.fcboard.service.dto.PostUpdateRequestDTO
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Post(
    title: String,
    content: String,
    createdBy: String,
) : BaseEntity(createdBy) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    var title: String = title
        protected set

    var content: String = content
        protected set

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = [CascadeType.ALL])
    var comments: MutableList<Comment> = mutableListOf()
        protected set

    fun update(postUpdateRequestDTO: PostUpdateRequestDTO) {
        if (postUpdateRequestDTO.updatedBy != this.createdBy) {
            throw PostNotUpdatableException()
        }

        this.title = postUpdateRequestDTO.title
        this.content = postUpdateRequestDTO.content
        super.updatedBy(postUpdateRequestDTO.updatedBy)
    }
}
