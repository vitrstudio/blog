package studio.vitr.blog.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@Configuration
class JacksonConfig {

    @Bean
    fun objectMapper(): ObjectMapper = jacksonObjectMapper().setPropertyNamingStrategy(SNAKE_CASE)

    @Bean
    fun httpMessageConverter(mapper: ObjectMapper) = MappingJackson2HttpMessageConverter(mapper)
} 