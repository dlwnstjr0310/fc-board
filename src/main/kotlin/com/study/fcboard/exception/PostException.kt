package com.study.fcboard.exception

open class PostException(message: String) : RuntimeException(message)

class PostNotFoundException : PostException("게시글을 찾을 수 없습니다.")

class PostNotUpdatableException : PostException("수정할 수 없는 게시물입니다.")

class PostNotDeletableException : PostException("게시물을 삭제 할 수 없습니다.")
