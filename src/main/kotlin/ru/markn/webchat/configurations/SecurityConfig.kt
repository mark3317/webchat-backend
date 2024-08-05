package ru.markn.webchat.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfig(
    private val jwtRequestFilter: JwtRequestFilter
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .csrf(CsrfConfigurer<HttpSecurity>::disable)
        .cors(CorsConfigurer<HttpSecurity>::disable)
        .authorizeHttpRequests { authorizeRequests ->
            authorizeRequests
                .requestMatchers("/profile").authenticated()
                .requestMatchers("/users","/users/**").hasRole("ADMIN")
                .anyRequest().permitAll()
        }
        .sessionManagement { session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .exceptionHandling { exception ->
            exception.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        }
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
        .build()

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager
}