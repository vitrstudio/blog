package studio.vitr.blog.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import studio.vitr.blog.model.User
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<User, UUID> {

    fun findByEmail(email: String): User?
}