package studio.vitr.seed.errors

class Forbidden(id: String) : Error("access forbidden for user $id")