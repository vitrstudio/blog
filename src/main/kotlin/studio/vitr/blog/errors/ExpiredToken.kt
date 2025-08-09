package studio.vitr.blog.errors

class ExpiredToken(tokenType: String) : Error("$tokenType is expired")