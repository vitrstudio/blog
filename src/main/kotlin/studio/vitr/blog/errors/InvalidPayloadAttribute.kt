package studio.vitr.blog.errors

class InvalidPayloadAttribute(attributeName: String, objectName: String): Error("invalid $attributeName in $objectName")