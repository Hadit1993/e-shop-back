package com.hadit1993.eshopback.repositories

import com.hadit1993.eshopback.entities.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository: MongoRepository<UserEntity, String> {

    fun findByEmail(email: String): UserEntity?
}