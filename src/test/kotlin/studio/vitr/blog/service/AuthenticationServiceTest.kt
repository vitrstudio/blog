package studio.vitr.blog.service

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import studio.vitr.blog.errors.ExpiredToken
import studio.vitr.blog.errors.InvalidParameter
import studio.vitr.blog.errors.InvalidPayloadAttribute
import studio.vitr.blog.model.SessionToken
import studio.vitr.blog.model.TokenType
import studio.vitr.blog.model.User
import studio.vitr.blog.model.api.SignInRequest
import studio.vitr.blog.utils.TimeUtil
import java.util.*

class AuthenticationServiceTest {

    private val userService = mockk<UserService>()
    private val jwtService = mockk<JwtService>()
    private lateinit var authenticationService: AuthenticationService

    @BeforeEach
    fun setUp() {
        authenticationService = AuthenticationService(userService, jwtService)
    }

    @Test
    fun `should generate tokens on successful sign in`() {
        // Given
        val userId = UUID.randomUUID()
        val email = "test@example.com"
        val password = "password123"
        val user = User(id = userId, email = email, password = "encoded_password")
        val accessToken = "access_token_123"
        val refreshToken = "refresh_token_456"

        every { userService.findByEmail(email) } returns user
        every { userService.verifyPassword(password, user.password) } returns true
        every { jwtService.generateAccessToken(userId.toString(), email) } returns accessToken
        every { jwtService.generateRefreshToken(userId.toString()) } returns refreshToken
        every { jwtService.getAccessTokenExpiration() } returns 900L

        val request = SignInRequest(email, password)

        // When
        val response = authenticationService.signIn(request)

        // Then
        assertEquals(accessToken, response.accessToken)
        assertEquals(refreshToken, response.refreshToken)
        assertEquals(900L, response.expiresIn)
    }

    @Test
    fun `should validate access token correctly`() {
        // Given
        val token = "valid_access_token"
        every { jwtService.validateToken(token) } returns true
        every { jwtService.extractSessionTokenAttributes(token) } returns SessionToken(
            type = TokenType.ACCESS,
            userId = "123e4567-e89b-12d3-a456-426614174000",
            email = "email@email.com",
            issuedAt = TimeUtil.now(),
            expiresAt = TimeUtil.add(TimeUtil.now(), 900000L)
        )

        // Then
        assertDoesNotThrow {
            authenticationService.validateAccessToken(token)
        }
    }

    @Test
    fun `should reject invalid access token`() {
        // Given
        val token = "invalid_token"
        every { jwtService.validateToken(token) } returns false

        // Then
        assertThrows<InvalidParameter> {
            authenticationService.validateAccessToken(token)
        }
    }

    @Test
    fun `should reject expired access token`() {
        // Given
        val token = "expired_token"
        every { jwtService.validateToken(token) } returns true
        every { jwtService.extractSessionTokenAttributes(token) } returns SessionToken(
            type = TokenType.ACCESS,
            userId = "123e4567-e89b-12d3-a456-426614174000",
            email = "email@email.com",
            issuedAt = TimeUtil.now(),
            expiresAt = TimeUtil.now() - 1L // expired
        )

        assertThrows<ExpiredToken> {
            authenticationService.validateAccessToken(token)
        }
    }

    @Test
    fun `should reject refresh token as access token`() {
        // Given
        val token = "refresh_token"
        every { jwtService.validateToken(token) } returns true
        every { jwtService.extractSessionTokenAttributes(token) } returns SessionToken(
            type = TokenType.REFRESH,
            userId = "123e4567-e89b-12d3-a456-426614174000",
            email = null,
            issuedAt = TimeUtil.now(),
            expiresAt = TimeUtil.add(TimeUtil.now(), 900_000L)
        )

        // Then
        assertThrows<InvalidPayloadAttribute> {
            authenticationService.validateAccessToken(token)
        }
    }
}
