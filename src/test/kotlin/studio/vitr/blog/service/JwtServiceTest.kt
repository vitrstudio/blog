package studio.vitr.blog.service

import io.jsonwebtoken.MalformedJwtException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.test.util.ReflectionTestUtils
import studio.vitr.blog.model.TokenType

class JwtServiceTest {

    @Test
    fun `should generate access token successfully`() {
        // Given
        val jwtService = JwtService()
        val userId = "123e4567-e89b-12d3-a456-426614174000"
        val email = "test@example.com"

        // Set secret and expiration for testing
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "test-secret-key-for-jwt-signing-that-is-long-enough")
        ReflectionTestUtils.setField(jwtService, "accessTokenExpiration", 900_000L)
        ReflectionTestUtils.setField(jwtService, "refreshTokenExpiration", 604_800_000L)

        // When
        val token = jwtService.generateAccessToken(userId, email)

        // Then
        assertNotNull(token)
        assertTrue(token.isNotEmpty())
        assertTrue(jwtService.validateToken(token))
        val attributes = jwtService.extractSessionTokenAttributes(token)
        assertEquals(userId, attributes.userId)
        assertEquals(email, attributes.email)
        assertEquals(TokenType.ACCESS, attributes.type)
        assertFalse(attributes.isExpired())
    }

    @Test
    fun `should generate refresh token successfully`() {
        // Given
        val jwtService = JwtService()
        val userId = "123e4567-e89b-12d3-a456-426614174000"
        
        // Set secret for testing
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "test-secret-key-for-jwt-signing-that-is-long-enough")
        
        // When
        val token = jwtService.generateRefreshToken(userId)
        
        // Then
        assertNotNull(token)
        assertTrue(token.isNotEmpty())
        assertTrue(jwtService.validateToken(token))
        val attributes = jwtService.extractSessionTokenAttributes(token)
        assertEquals(userId, attributes.userId)
        assertEquals(TokenType.REFRESH, attributes.type)
        assertFalse(attributes.isExpired())
    }

    @Test
    fun `should reject invalid token`() {
        // Given
        val jwtService = JwtService()
        val invalidToken = "invalid.token.here"
        
        // Set secret for testing
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "test-secret-key-for-jwt-signing-that-is-long-enough")

        // When & Then
        assertFalse(jwtService.validateToken(invalidToken))

        assertThrows<MalformedJwtException> {
            jwtService.extractSessionTokenAttributes(invalidToken)
        }
    }

    @Test
    fun `should extract correct values from valid token`() {
        // Given
        val jwtService = JwtService()
        val userId = "123e4567-e89b-12d3-a456-426614174000"
        val email = "test@example.com"
        
        // Set secret for testing
        ReflectionTestUtils.setField(jwtService, "jwtSecret", "test-secret-key-for-jwt-signing-that-is-long-enough")
        
        val token = jwtService.generateAccessToken(userId, email)
        
        // When & Then
        val attributes = jwtService.extractSessionTokenAttributes(token)
        assertEquals(userId, attributes.userId)
        assertEquals(email, attributes.email)
        assertEquals(TokenType.ACCESS, attributes.type)
        assertFalse(attributes.isExpired())
    }
}
