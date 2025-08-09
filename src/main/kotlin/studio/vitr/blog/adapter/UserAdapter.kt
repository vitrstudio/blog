package studio.vitr.blog.adapter

import org.springframework.stereotype.Component
import studio.vitr.blog.constants.Properties.ID
import studio.vitr.blog.errors.InvalidParameter
import studio.vitr.blog.model.User
import studio.vitr.blog.model.api.UserResponse

@Component
class UserAdapter {
    fun toUserResponse(u: User) = UserResponse(
        id = u.id ?: throw InvalidParameter(ID),
        email = u.email,
        createdAt = u.createdAt,
    )
}