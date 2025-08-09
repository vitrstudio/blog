package studio.vitr.blog.model

class Session(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)