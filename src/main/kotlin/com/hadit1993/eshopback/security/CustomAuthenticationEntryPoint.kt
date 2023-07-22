package com.hadit1993.eshopback.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.hadit1993.eshopback.dtos.response.BaseResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class CustomAuthenticationEntryPoint: AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    )
    {

        val baseResponse = BaseResponse<Unit>(success = false, message =  "missing or invalid authentication credentials")
        response.apply {
            status = HttpStatus.UNAUTHORIZED.value()
            writer.print(ObjectMapper().writeValueAsString(baseResponse))
            contentType = "application/json"
        }

    }
}