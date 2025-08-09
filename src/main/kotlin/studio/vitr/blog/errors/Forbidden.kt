package studio.vitr.blog.errors

class Forbidden(id: String) : Error("access forbidden for user $id")