package com.hadit1993.eshopback.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.boot.logging.LogLevel
import org.springframework.boot.logging.LoggingSystem

@Configuration
@PropertySources(
    PropertySource(value = ["classpath:application.properties"], ignoreResourceNotFound = true),
    PropertySource(value = ["classpath:application.yml"], ignoreResourceNotFound = true)
)
class LoggingConfiguration {

    @Bean
    fun loggingSystem(): LoggingSystem {
        val loggingSystem = LoggingSystem.get(javaClass.classLoader)
        loggingSystem.setLogLevel("com.hadit1993.eshop", LogLevel.DEBUG)
        return loggingSystem
    }
}
