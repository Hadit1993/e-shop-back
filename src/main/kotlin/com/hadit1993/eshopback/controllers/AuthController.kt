package com.hadit1993.eshopback.controllers

import com.hadit1993.eshopback.dtos.request.LoginDto
import com.hadit1993.eshopback.dtos.request.RegisterUserDto
import com.hadit1993.eshopback.dtos.response.BaseResponse
import com.hadit1993.eshopback.dtos.response.UserProfileDto
import com.hadit1993.eshopback.security.jwt.JwtTokenType
import com.hadit1993.eshopback.services.*
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID


@RestController
@Validated
@RequestMapping("/api")
class AuthController(
    private val authService: AuthService,
    private val userService: UserService,
    private val mailService: MailService,
    private val redisService: RedisService

) {

    @PostMapping("/register")
    fun registerUser(
        @Valid @ModelAttribute user: RegisterUserDto,
        @RequestParam("profileImage") profileImage: MultipartFile?
    ): ResponseEntity<BaseResponse<out Any>> {
        if ((profileImage != null) && (profileImage.contentType?.startsWith("image/") == false)) {
            return ResponseEntity.badRequest().body(
                BaseResponse(
                    mapOf("profileImage" to "Only image files are allowed"),
                    false,
                    "Some inputs are invalid"
                )
            );
        }
        val userEntity = userService.createUser(user, profileImage)
        val token = authService.registerUser(userEntity)
        val tokenUUID = UUID.randomUUID().toString()
        redisService.setValue(tokenUUID, token)
        mailService.sendActivationUrl(user.email!!, tokenUUID, EmailType.AccountActivation)
        return ResponseEntity(
            BaseResponse<Unit>(

                message = "Activation token sent to your email",
                success = true
            ), HttpStatus.CREATED
        )
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody user: LoginDto): ResponseEntity<BaseResponse<Any>>{
        val token = authService.loginUser(user)

        return ResponseEntity(
            BaseResponse(
                data = mapOf("accessToken" to token),
                message = "You logged in successfully",
                success = true
            ), HttpStatus.OK
        )
    }

    @GetMapping("/activate/{activationTokenId}")
    fun activateUser(@PathVariable activationTokenId: String): ResponseEntity<BaseResponse<Any>> {
     val token = redisService.getValue(activationTokenId) as String?
         ?: return ResponseEntity(BaseResponse(success = false, message = "invalid token id"), HttpStatus.BAD_REQUEST)

        val claims = authService.verifyToken(token, JwtTokenType.AccountActivation)
      userService.activateUser(claims.subject)
        return ResponseEntity.ok(BaseResponse(success = true, message = "Your account activated successfully"))
    }

    @GetMapping("/profile")
    fun getProfile(): ResponseEntity<BaseResponse<Any>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = userService.getUser(authentication.name)
        return ResponseEntity(BaseResponse(UserProfileDto.fromEntity(user), true), HttpStatus.OK)
    }
}