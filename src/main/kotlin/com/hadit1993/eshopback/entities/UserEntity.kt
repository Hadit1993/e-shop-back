package com.hadit1993.eshopback.entities


import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

import java.util.*

@Document("users")
data class UserEntity(
    @Id
    val id: String? = null,
    val name: String,
    @Indexed(unique = true)
    val email: String,
    val password: String,
    val phoneNumber: Long? = null,
    val addresses: List<AddressEntity> = emptyList(),
    val profileImage: String? = null,
    val createdAt: Date = Date(),
    val resetPasswordToken: String? = null,
    val resetPasswordTime: Date? = null,
    val roles: List<String> = listOf("USER"),
    val isActive: Boolean = false
)


