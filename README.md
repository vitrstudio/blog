# Blog API
Blog is a RESTful API template.

## Run app locally
`docker-compose -f docker-compose.yml up -d`

Make sure your .env variables are properly set in your IDE before running the app.

## Password Encryption

This application uses BCrypt for password encryption. All passwords are automatically encrypted before being stored in the database.

### Features:
- **Automatic Encryption**: Passwords are encrypted using BCrypt when creating users
- **Secure Hashing**: BCrypt provides salt and cost factor for enhanced security
- **Password Verification**: Built-in methods to verify passwords during login

### Security Configuration
The application includes a `SecurityConfig` that disables default Spring Security authentication while keeping BCrypt available for password encoding.

