package studio.vitr.blog.errors

class InvalidParameter(param: String): Error("invalid $param")