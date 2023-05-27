package com.hadit1993.eshopback.entities

import org.springframework.data.annotation.Id

data class AddressEntity(
    @Id
    val id: String? = null,
    val country: String,
    val city: String,
    val address1: String,
    val address2: String?,
    val zipCode: Int,
    val addressType: String
)
