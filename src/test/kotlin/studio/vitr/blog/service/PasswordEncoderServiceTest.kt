package studio.vitr.blog.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class PasswordEncoderServiceTest {

    private val passwordEncoderService = PasswordEncoderService()

    @Test
    fun `should encode password successfully`() {
        // Given
        val rawPassword = "myPassword123"
        
        // When
        val encodedPassword = passwordEncoderService.encodePassword(rawPassword)
        
        // Then
        assertNotNull(encodedPassword)
        assertNotEquals(rawPassword, encodedPassword)
        assertTrue(encodedPassword.startsWith("\$2a\$"))
    }

    @Test
    fun `should verify correct password`() {
        // Given
        val rawPassword = "myPassword123"
        val encodedPassword = passwordEncoderService.encodePassword(rawPassword)
        
        // When
        val isValid = passwordEncoderService.verifyPassword(rawPassword, encodedPassword)
        
        // Then
        assertTrue(isValid)
    }

    @Test
    fun `should reject incorrect password`() {
        // Given
        val correctPassword = "myPassword123"
        val wrongPassword = "wrongPassword"
        val encodedPassword = passwordEncoderService.encodePassword(correctPassword)
        
        // When
        val isValid = passwordEncoderService.verifyPassword(wrongPassword, encodedPassword)
        
        // Then
        assertFalse(isValid)
    }

    @Test
    fun `should generate different hashes for same password`() {
        // Given
        val password = "myPassword123"
        
        // When
        val hash1 = passwordEncoderService.encodePassword(password)
        val hash2 = passwordEncoderService.encodePassword(password)
        
        // Then
        assertNotEquals(hash1, hash2)
        assertTrue(passwordEncoderService.verifyPassword(password, hash1))
        assertTrue(passwordEncoderService.verifyPassword(password, hash2))
    }
}
