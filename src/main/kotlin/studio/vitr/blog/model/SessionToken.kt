package studio.vitr.blog.model

import studio.vitr.blog.utils.TimeUtil

class SessionToken(
    val type: TokenType,
    val userId: String,
    val email: String?,
    val issuedAt: Long,
    val expiresAt: Long
) {
    fun isExpired() = TimeUtil.now() > expiresAt
}