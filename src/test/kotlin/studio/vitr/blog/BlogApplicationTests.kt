package studio.vitr.blog

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(properties = [
    "PROJECT_NAME=blogtest",
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=test",
    "spring.datasource.password=test",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "JWT_SECRET=558153b5-4ec0-419e-90ad-d54d377f0b9f"
])
class BlogApplicationTests {

    @Test
    fun contextLoads() {}
}
