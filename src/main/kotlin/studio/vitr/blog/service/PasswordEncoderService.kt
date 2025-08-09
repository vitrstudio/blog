package studio.vitr.blog.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class PasswordEncoderService {
    
    private val passwordEncoder = BCryptPasswordEncoder()

    fun encodePassword(rawPassword: String): String = passwordEncoder.encode(rawPassword)

    fun verifyPassword(raw: String, encoded: String) = passwordEncoder.matches(raw, encoded)
}
