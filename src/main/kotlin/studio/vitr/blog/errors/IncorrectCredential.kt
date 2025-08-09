package studio.vitr.blog.errors

class IncorrectCredential(param: String): Error("incorrect credential $param")