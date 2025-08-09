package studio.vitr.blog.controller

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import studio.vitr.blog.adapter.UserAdapter
import studio.vitr.blog.constants.Properties.USER
import studio.vitr.blog.errors.Forbidden
import studio.vitr.blog.errors.NotFound
import studio.vitr.blog.model.api.CreateUserRequest
import studio.vitr.blog.model.api.PasswordVerificationRequest
import studio.vitr.blog.model.api.PasswordVerificationResponse
import studio.vitr.blog.service.UserService
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val adapter: UserAdapter,
) {

    @GetMapping
    fun getUsers() = userService.getAll()
        .map { adapter.toUserResponse(it) }

    @GetMapping("/me")
    fun getCurrentUser() = authenticatedUsername()
        .let { UUID.fromString(it) }
        .let { userService.get(it) }
        ?.let { adapter.toUserResponse(it) }
        ?: throw Forbidden(authenticatedUsername())

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: UUID) = userService.get(userId)
        ?.let { adapter.toUserResponse(it) }
        ?: throw NotFound(USER, userId.toString())

    @PostMapping
    fun createUser(@RequestBody request: CreateUserRequest) = userService
        .create(request.email, request.password)
        .let { adapter.toUserResponse(it) }

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: UUID) = userService.delete(userId)
        
    @PostMapping("/{userId}/verify-password")
    fun verifyPassword(
        @PathVariable userId: UUID,
        @RequestBody request: PasswordVerificationRequest
    ) = userService
        .verifyPassword(userId, request.password)
        .let { PasswordVerificationResponse(isValid = it) }

    private fun authenticatedUsername() = SecurityContextHolder.getContext().authentication.name
}
