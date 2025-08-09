package studio.vitr.blog.model

import studio.vitr.blog.constants.Properties.TOKEN_TYPE
import studio.vitr.blog.errors.InvalidEnumValue

enum class TokenType {
    ACCESS,
    REFRESH;

    companion object {
        fun fromString(value: String) = entries
            .find { it.toString() == value }
            ?: throw InvalidEnumValue(TOKEN_TYPE, value)
    }
}