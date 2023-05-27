package com.hadit1993.eshopback.utils.error

import com.hadit1993.eshopback.dtos.response.BaseResponse
import io.jsonwebtoken.ExpiredJwtException
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

import org.springframework.http.HttpStatus

import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException


import org.springframework.web.bind.MethodArgumentNotValidException

import org.springframework.web.bind.annotation.ExceptionHandler

import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class GlobalExceptionHandler {


    private val appLogger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)


    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException): ResponseEntity<BaseResponse<Any>> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage) }

        return ResponseEntity(
            BaseResponse(
                errors,
                false,
                if (errors.isNotEmpty()) "some inputs are invalid" else ex.message
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(HttpClientErrorException::class)
    fun handleHttpClientException(exception: HttpClientErrorException): ResponseEntity<BaseResponse<Any>> {
        return ResponseEntity(BaseResponse(null, false, exception.statusText), exception.statusCode)
    }


    @ExceptionHandler(HttpException::class)
    fun handleHttpClass(e: HttpException): ResponseEntity<BaseResponse<Any>> {
        return ResponseEntity(BaseResponse(e.data, false, e.message), e.statusCode)
    }


    @ExceptionHandler(ExpiredJwtException::class)
    fun handleHttpClass(e: ExpiredJwtException): ResponseEntity<BaseResponse<Any>> {
        return ResponseEntity(BaseResponse(null, false, "Your token has expired"), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthException(e: AuthenticationException): ResponseEntity<BaseResponse<Any>> {
        if (e.cause is HttpException) {
            val httpException = (e.cause as HttpException)
            return ResponseEntity(
                BaseResponse(
                    httpException.data,
                    false,
                    httpException.message
                ), httpException.statusCode
            )
        }

        return ResponseEntity(
            BaseResponse(
                success = false,
                message = e.message ?: "missing or invalid authentication credentials"
            ), HttpStatus.UNAUTHORIZED
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleUnknownException(exception: Exception): ResponseEntity<BaseResponse<Any>> {
        appLogger.error("error happened: $exception")

        return ResponseEntity(
            BaseResponse(
                null,
                false,
                exception.message ?: "An unknown error occurred. Please try again later"
            ), HttpStatus.INTERNAL_SERVER_ERROR
        )
    }


}