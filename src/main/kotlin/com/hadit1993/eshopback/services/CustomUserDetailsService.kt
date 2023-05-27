package com.hadit1993.eshopback.services

import com.hadit1993.eshopback.repositories.UserRepository
import com.hadit1993.eshopback.utils.error.BadRequestException
import com.hadit1993.eshopback.utils.error.NotFoundException
import org.springframework.security.core.authority.SimpleGrantedAuthority

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(val userRepository: UserRepository) : UserDetailsService {


    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
            ?: throw NotFoundException("No user found with email: $email")
        if (!user.isActive) throw BadRequestException("User is not active yet")
        return User.withUsername(user.email)
            .password(user.password)
            .roles(*user.roles.toTypedArray())
            .authorities(user.roles.map { SimpleGrantedAuthority("ROLE_${it}") })
            .build()
    }
}


