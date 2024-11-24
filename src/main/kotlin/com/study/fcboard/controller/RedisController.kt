package com.study.fcboard.controller

import com.study.fcboard.util.RedisUtil
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RedisController(
    private val redisUtil: RedisUtil,
) {

    @GetMapping("/redis")
    fun getRedisCount(): Long {
        redisUtil.increment("count")
        return redisUtil.getCount("count") ?: 0L
    }
}
