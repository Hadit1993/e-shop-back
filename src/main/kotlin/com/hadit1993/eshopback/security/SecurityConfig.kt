package com.hadit1993.eshopback.security


import com.hadit1993.eshopback.security.jwt.JwtFilter
import com.hadit1993.eshopback.utils.error.CustomAccessDeniedHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity(debug = true)
class SecurityConfig(
    private val jwtFilter: JwtFilter,
    private val userDetailsService: UserDetailsService

) {


    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {


        http.csrf { csrf ->
            csrf.disable()

        }.authorizeHttpRequests { authZ ->
            authZ
                .requestMatchers("/api/register", "/api/login", "/api/activate/**" ,"/images/**").permitAll()
                .anyRequest()
                .authenticated()

        }.exceptionHandling { ehc ->
            ehc.accessDeniedHandler(customAccessDeniedHandler()).authenticationEntryPoint(customAuthEntryPoint())

        }.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .httpBasic(Customizer.withDefaults()).authenticationProvider(authenticationProvider())


        return http.build()
    }


    @Bean
    fun customAccessDeniedHandler(): AccessDeniedHandler {
        return CustomAccessDeniedHandler()
    }

    @Bean
    fun customAuthEntryPoint(): AuthenticationEntryPoint {
        return CustomAuthenticationEntryPoint()
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val daoAuthenticationProvider = DaoAuthenticationProvider()
        daoAuthenticationProvider.setUserDetailsService(userDetailsService)
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder())
        return daoAuthenticationProvider
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }


}
