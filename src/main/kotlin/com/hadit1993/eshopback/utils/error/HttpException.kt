package com.hadit1993.eshopback.utils.error

import org.springframework.http.HttpStatus

sealed class HttpException(override val message: String, val statusCode: HttpStatus, val data: Any? = null) : RuntimeException(message)

 class BadRequestException(override val message: String, data: Any? = null): HttpException(message, HttpStatus.BAD_REQUEST, data)
 class NotFoundException(override val message: String): HttpException(message, HttpStatus.NOT_FOUND)
 class UnAuthorizedException(override val message: String): HttpException(message, HttpStatus.UNAUTHORIZED)
 class UnknownException(override val message: String): HttpException(message, HttpStatus.INTERNAL_SERVER_ERROR)