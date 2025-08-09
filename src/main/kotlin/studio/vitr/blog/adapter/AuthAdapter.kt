package studio.vitr.blog.adapter

import org.springframework.stereotype.Component
import studio.vitr.blog.model.Session
import studio.vitr.blog.model.api.RefreshTokenResponse
import studio.vitr.blog.model.api.SignInResponse

@Component
class AuthAdapter {

    fun toSignUpResponse(s: Session) = SignInResponse(
        accessToken = s.accessToken,
        refreshToken = s.refreshToken,
    )

    fun toSignInResponse(s: Session) = SignInResponse(
        accessToken = s.accessToken,
        refreshToken = s.refreshToken,
    )

    fun toRefreshTokenResponse(s: Session) = RefreshTokenResponse(accessToken = s.accessToken)
}