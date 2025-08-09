package studio.vitr.blog.model.api

data class Health(
    val status: HealthStatus,
    val version: String
)