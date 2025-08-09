package studio.vitr.blog.integrations

import org.springframework.stereotype.Service

@Service
class GithubClient {

    fun createRepository(githubUserId: Long, repositoryName: String) = randomLong() // Returns a mock repository ID

    fun randomLong() = (0..9999L).random()
}