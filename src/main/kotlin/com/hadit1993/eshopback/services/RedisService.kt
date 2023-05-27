package com.hadit1993.eshopback.services

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisService(private val redisTemplate: RedisTemplate<String, Any>) {
    fun setValue(key: String, value: Any?) {
        redisTemplate.opsForValue()[key] = value!!
    }

    fun getValue(key: String): Any? {
        return redisTemplate.opsForValue()[key]
    }

}