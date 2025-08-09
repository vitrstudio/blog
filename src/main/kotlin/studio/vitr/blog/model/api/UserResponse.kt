package studio.vitr.blog.model.api

import java.util.*

class UserResponse(
    val id: UUID,
    val email: String,
    val createdAt: Long
)