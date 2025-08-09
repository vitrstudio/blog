package studio.vitr.blog.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfigurationSource
import studio.vitr.blog.service.AuthenticationService
import studio.vitr.blog.service.UserService

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val corsConfigurationSource: CorsConfigurationSource
) {

    @Bean
    @Order(1)
    fun publicSecurityChain(http: HttpSecurity): SecurityFilterChain = http
        .securityMatchers { it
            .requestMatchers("/health")
            .requestMatchers("/auth/**")
        }
        .cors { it.configurationSource(corsConfigurationSource) }
        .csrf { it.disable() }
        .sessionManagement { it.sessionCreationPolicy(STATELESS) }
        .authorizeHttpRequests { it.anyRequest().permitAll() }
        .build()

    @Bean
    @Order(2)
    fun protectedSecurityChain(http: HttpSecurity): SecurityFilterChain = http
        .cors { it.configurationSource(corsConfigurationSource) }
        .csrf { it.disable() }
        .sessionManagement { it.sessionCreationPolicy(STATELESS) }
        .authorizeHttpRequests { it.anyRequest().authenticated() }
        .addFilterBefore(JwtAuthenticationFilter(authenticationService, userService), UsernamePasswordAuthenticationFilter::class.java)
        .build()
}
