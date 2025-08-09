package studio.vitr.blog.model.api

data class SignInResponse(
    val accessToken: String,
    val refreshToken: String,
)
