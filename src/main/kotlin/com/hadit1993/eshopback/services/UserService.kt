package com.hadit1993.eshopback.services

import com.hadit1993.eshopback.configs.ImageType
import com.hadit1993.eshopback.configs.ImageUploader
import com.hadit1993.eshopback.dtos.request.RegisterUserDto
import com.hadit1993.eshopback.entities.UserEntity
import com.hadit1993.eshopback.repositories.UserRepository
import com.hadit1993.eshopback.utils.error.BadRequestException
import com.hadit1993.eshopback.utils.error.NotFoundException

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import org.springframework.web.multipart.MultipartFile


@Service
class UserService(private val repository: UserRepository, private val passwordEncoder: PasswordEncoder, private val imageUploader: ImageUploader) {

    fun createUser(user: RegisterUserDto, profileImage: MultipartFile?): UserEntity {
        val existingUser = repository.findByEmail(user.email!!)

        if (existingUser != null) throw  BadRequestException("This email is already used")

        var imageUrl: String? = null
        if(profileImage != null) {
            imageUrl = imageUploader.uploadImage(profileImage, ImageType.user)
        }
        return repository.save(
            user.toEntity()
                .copy(profileImage = imageUrl, password = passwordEncoder.encode(user.password))
        )

    }

    fun activateUser(email: String) {

        val user = getUser(email).copy(isActive = true)
        repository.save(user)
    }

    fun getUser(email: String): UserEntity {
        return repository.findByEmail(email) ?: throw NotFoundException("User not found with email: $email")
    }
}