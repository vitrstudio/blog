package studio.vitr.seed.model.api

data class SignInResponse(
    val accessToken: String,
    val refreshToken: String,
)
