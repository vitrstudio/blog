package studio.vitr.blog.errors

class MissingExpectedParameter(param: String): Error("missing expected $param")