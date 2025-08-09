package studio.vitr.seed.service

import org.springframework.stereotype.Service
import studio.vitr.seed.constants.Properties.ACCESS_TOKEN
import studio.vitr.seed.constants.Properties.PASSWORD
import studio.vitr.seed.constants.Properties.REFRESH_TOKEN
import studio.vitr.seed.constants.Properties.TOKEN_TYPE
import studio.vitr.seed.constants.Properties.USER
import studio.vitr.seed.constants.Properties.USER_ID
import studio.vitr.seed.errors.*
import studio.vitr.seed.model.Session
import studio.vitr.seed.model.TokenType.ACCESS
import studio.vitr.seed.model.TokenType.REFRESH
import studio.vitr.seed.model.User
import studio.vitr.seed.model.api.RefreshTokenRequest
import studio.vitr.seed.model.api.SignInRequest
import studio.vitr.seed.model.api.SignUpRequest
import java.util.*

@Service
class AuthenticationService(
    private val userService: UserService,
    private val jwtService: JwtService
) {

    fun signUp(request: SignUpRequest) = userService
        .create(request.email, request.password)
        .let{ session(it) }

    fun signIn(request: SignInRequest) = userService
        .findByEmail(request.email)
        ?.let { signIn(it, request.password) }
        ?: throw NotFound(USER, request.email)

    fun refreshToken(request: RefreshTokenRequest): Session {
        val refreshToken = request.refreshToken
        if (!jwtService.validateToken(refreshToken)) throw InvalidParameter(REFRESH_TOKEN)

        val attributes = jwtService.extractSessionTokenAttributes(refreshToken)
        if (attributes.isExpired()) throw ExpiredToken(REFRESH_TOKEN)
        if (attributes.type != REFRESH) throw InvalidPayloadAttribute(TOKEN_TYPE, REFRESH_TOKEN)

        val userId = attributes.userId
        val user = userService.get(UUID.fromString(userId)) ?: throw NotFound(USER, userId)
        return session(refreshToken, user)
    }

    fun validateAccessToken(token: String) {
        if (!jwtService.validateToken(token)) throw InvalidParameter(ACCESS_TOKEN)
        val attributes = jwtService.extractSessionTokenAttributes(token)
        if (attributes.type != ACCESS) throw InvalidPayloadAttribute(TOKEN_TYPE, REFRESH_TOKEN)
        if (attributes.isExpired()) throw ExpiredToken(ACCESS_TOKEN)
    }

    fun getUserId(token: String) = jwtService
        .extractSessionTokenAttributes(token).userId
        .takeIf { it.isNotBlank() }
        ?.let { UUID.fromString(it) }
        ?: throw InvalidPayloadAttribute(USER_ID, ACCESS_TOKEN)

    private fun signIn(user: User, password: String) = user
        .takeIf { userService.verifyPassword(password, it.password) }
        ?.let { session(it) }
        ?: throw IncorrectCredential(PASSWORD)

    private fun session(user: User) = Session(
        accessToken = jwtService.generateAccessToken(user.idStr(), user.email),
        refreshToken = jwtService.generateRefreshToken(user.idStr()),
        expiresIn = jwtService.getAccessTokenExpiration()
    )

    private fun session(refreshToken: String, user: User) = Session(
        accessToken = jwtService.generateAccessToken(user.idStr(), user.email),
        refreshToken = refreshToken,
        expiresIn = jwtService.getAccessTokenExpiration()
    )
}
