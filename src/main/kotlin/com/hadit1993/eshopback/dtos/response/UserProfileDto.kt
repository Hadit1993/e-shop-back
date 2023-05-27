package com.hadit1993.eshopback.dtos.response

import com.hadit1993.eshopback.entities.UserEntity

data class UserProfileDto(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: Long? = null,
    val profileImage: String? = null,

    ) {

    companion object {
        fun fromEntity(userEntity: UserEntity): UserProfileDto {
            return UserProfileDto(
                id = userEntity.id!!,
                name = userEntity.name,
                email = userEntity.email,
                phoneNumber = userEntity.phoneNumber,
                profileImage = userEntity.profileImage
            )
        }
    }
}