package com.hadit1993.eshopback.utils.error

import com.fasterxml.jackson.databind.ObjectMapper
import com.hadit1993.eshopback.dtos.response.BaseResponse
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import java.io.IOException


class CustomAccessDeniedHandler : AccessDeniedHandler {
    @Throws(IOException::class, ServletException::class)
    override fun handle(request: HttpServletRequest, response: HttpServletResponse, exception: AccessDeniedException) {
        val baseResponse = BaseResponse<Unit>(success = false, message =  "missing or invalid authentication credentials")
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.writer.print(ObjectMapper().writeValueAsString(baseResponse))
        response.contentType = "application/json"

    }
}

