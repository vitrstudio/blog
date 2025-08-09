package studio.vitr.blog.service

import org.springframework.stereotype.Service
import studio.vitr.blog.constants.Properties.USER
import studio.vitr.blog.errors.NotFound
import studio.vitr.blog.model.User
import studio.vitr.blog.repository.UserRepository
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoderService: PasswordEncoderService
) {

    fun getAll(): List<User> = userRepository.findAll()

    fun get(id: UUID): User? = userRepository
        .findById(id)
        .orElse(null)

    fun create(email: String, password: String) = passwordEncoderService
        .encodePassword(password)
        .let { User(email = email, password = it) }
        .let { userRepository.save(it) }

    fun delete(userId: UUID) = get(userId)
        ?.let { userRepository.delete(it) }
        ?: throw NotFound(USER, userId.toString())

    fun verifyPassword(userId: UUID, password: String) = get(userId)
        ?.let { verifyPassword(password, it.password) }
        ?: throw NotFound(USER, userId.toString())

    fun verifyPassword(raw: String, encoded: String) = passwordEncoderService
        .verifyPassword(raw, encoded)

    fun findByEmail(email: String) = userRepository.findByEmail(email)
}