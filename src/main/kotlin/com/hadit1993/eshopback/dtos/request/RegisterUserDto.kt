package com.hadit1993.eshopback.dtos.request



import com.hadit1993.eshopback.entities.UserEntity
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class RegisterUserDto (
    @field:NotNull(message = "name is required")
    @field:NotBlank(message = "name must not be empty")
    val name: String?,
    @field:NotNull(message = "email is required")
    @field:NotBlank(message = "email must not be empty")
    @field:Email(message = "email is invalid")
    val email: String?,
    @field:NotNull(message = "password is required")
    @field:NotBlank(message = "password must not be empty")
    val password: String?,
//    var profileImage: String?
) {

    fun toEntity(): UserEntity {
        return UserEntity(name=name!!, email = email!!, password = password!!)
    }
}
