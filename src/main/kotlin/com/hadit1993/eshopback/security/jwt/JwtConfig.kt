package com.hadit1993.eshopback.security.jwt

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.security.KeyPair
import java.security.KeyStore
import java.security.PrivateKey

@Configuration
class JwtConfig(@Value("\${keystore.alias}") private val keystoreAlias: String,
                @Value("\${keystore.password}") private val keystorePassword: String,) {
    @Bean
    fun keyPair(): KeyPair {
        val inputStream = ClassPathResource("mykeystore.p12").inputStream
        val password = keystorePassword.toCharArray()

        val keyStore = KeyStore.getInstance("PKCS12")
        keyStore.load(inputStream, password)


        val privateKey = keyStore.getKey(keystoreAlias, password) as PrivateKey
        val publicKey = keyStore.getCertificate(keystoreAlias).publicKey

        return KeyPair(publicKey, privateKey)
    }
}