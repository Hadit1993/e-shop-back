package com.hadit1993.eshopback.services

import com.hadit1993.eshopback.dtos.request.LoginDto
import com.hadit1993.eshopback.entities.UserEntity
import com.hadit1993.eshopback.security.jwt.JwtService
import com.hadit1993.eshopback.security.jwt.JwtTokenType
import io.jsonwebtoken.Claims

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service


@Service
class AuthService(private val authenticationProvider: DaoAuthenticationProvider, private val jwtService: JwtService) {



    fun registerUser(user: UserEntity): String {

        return jwtService.generateToken(user.email, user.roles, JwtTokenType.AccountActivation)

    }


    fun verifyToken(token: String, tokenType: JwtTokenType): Claims {
       return jwtService.verify(token, tokenType)
    }

    fun loginUser(user: LoginDto): String {

        val authentication =
            authenticationProvider.authenticate(UsernamePasswordAuthenticationToken(user.email, user.password))
        SecurityContextHolder.getContext().authentication = authentication
        return jwtService.generateToken(
            authentication.name,
            authentication.authorities.map { grantedAuthority -> grantedAuthority.authority }, JwtTokenType.Authentication)
    }


}