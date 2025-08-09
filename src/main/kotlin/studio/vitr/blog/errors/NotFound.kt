package studio.vitr.blog.errors

class NotFound(entity: String, id: String): Error("$entity $id not found")