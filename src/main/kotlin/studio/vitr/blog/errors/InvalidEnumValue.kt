package studio.vitr.blog.errors

class InvalidEnumValue(enumType: String, value: String): Error("invalid enum value: $enumType - $value")