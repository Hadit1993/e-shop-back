package com.hadit1993.eshopback.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.hadit1993.eshopback.dtos.response.BaseResponse
import com.hadit1993.eshopback.utils.error.UnAuthorizedException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(private val jwtService: JwtService, private val userDetailsService: UserDetailsService) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorizationHeader = request.getHeader("Authorization")

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val token = authorizationHeader.substring(7)

            try {
                val claims = jwtService.verify(token, JwtTokenType.Authentication)
                val username = claims.subject
                val authorities = claims["authorities"] as List<String>
                 userDetailsService.loadUserByUsername(username)
                val authentication = UsernamePasswordAuthenticationToken(username, null, authorities.map {
                    SimpleGrantedAuthority(it)
                })
                SecurityContextHolder.getContext().authentication = authentication
            }

            catch (e: UnAuthorizedException) {
                val baseResponse = BaseResponse<Unit>(success = false, message =  e.message)
                response.status = e.statusCode.value()
                response.writer.print(ObjectMapper().writeValueAsString(baseResponse))
                response.contentType = "application/json"
            }
            catch (e: Exception) {
                SecurityContextHolder.clearContext()
                val baseResponse = BaseResponse<Unit>(success = false, message =  "Invalid JWT token")
                response.status = HttpStatus.UNAUTHORIZED.value()
                response.writer.print(ObjectMapper().writeValueAsString(baseResponse))
                response.contentType = "application/json"
                return
            }
        }

        filterChain.doFilter(request, response)
    }
}