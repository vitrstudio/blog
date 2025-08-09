package studio.vitr.blog.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            // Allow specific origins for both local development and production
            allowedOrigins = listOf(
                "http://localhost:5173",
                "https://blog.vitr.studio",
                "https://www.blog.vitr.studio"
            )

            // Allow specific HTTP methods
            allowedMethods = listOf(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
            )

            // Allow specific headers that your frontend might send
            allowedHeaders = listOf(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
            )

            // Allow credentials (cookies, authorization headers)
            allowCredentials = true

            // Cache preflight response for 1 hour
            maxAge = 3600L

            // Expose headers that the frontend can access
            exposedHeaders = listOf(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
            )
        }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }
}