package studio.vitr.blog.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import studio.vitr.blog.constants.Claims.EMAIL
import studio.vitr.blog.constants.Properties.TYPE
import studio.vitr.blog.model.SessionToken
import studio.vitr.blog.model.TokenType
import studio.vitr.blog.model.TokenType.ACCESS
import studio.vitr.blog.model.TokenType.REFRESH
import studio.vitr.blog.utils.TimeUtil
import java.util.*

@Service
class JwtService {

    @Value("\${jwt.secret:defaultSecretKeyForDevelopmentOnly}")
    private lateinit var jwtSecret: String

    @Value("\${jwt.access-token.expiration:900000}") // 15 minutes
    private var accessTokenExpiration: Long = 900_000

    @Value("\${jwt.refresh-token.expiration:604800000}") // 7 days
    private var refreshTokenExpiration: Long = 604_800_000L

    fun generateAccessToken(userId: String, email: String): String = generateAccessToken(userId, email, TimeUtil.now())

    fun generateRefreshToken(userId: String): String = generateRefreshToken(userId, TimeUtil.now())

    fun validateToken(token: String): Boolean = runCatching { validateTokenCatching(token) }.isSuccess

    fun extractSessionTokenAttributes(token: String) = Jwts.parserBuilder()
        .setSigningKey(key()).build()
        .parseClaimsJws(token).body
        .let { sessionToken(it) }

    fun sessionToken(claims: Claims) = SessionToken(
        userId = claims.subject,
        email = claims.get(EMAIL, String::class.java),
        type = claims.get(TYPE, String::class.java).let { TokenType.fromString(it) },
        issuedAt = claims.issuedAt.time,
        expiresAt = claims.expiration.time
    )

    fun getAccessTokenExpiration() = accessTokenExpiration

    private fun generateAccessToken(userId: String, email: String, timestamp: Long): String = Jwts.builder()
        .setSubject(userId)
        .claim(EMAIL, email)
        .claim(TYPE, ACCESS.toString())
        .setIssuedAt(Date(timestamp))
        .setExpiration(exp(timestamp, accessTokenExpiration))
        .signWith(key())
        .compact()

    private fun generateRefreshToken(userId: String, timestamp: Long): String = Jwts.builder()
        .setSubject(userId)
        .claim(TYPE, REFRESH.toString())
        .setIssuedAt(Date(timestamp))
        .setExpiration(exp(timestamp, refreshTokenExpiration))
        .signWith(key())
        .compact()

    private fun validateTokenCatching(token: String) = Jwts.parserBuilder()
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token)

    private fun exp(t: Long, duration: Long) = TimeUtil.getExpirationDate(t, duration)

    private fun key() = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
}
