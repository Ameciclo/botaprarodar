package app.igormatos.botaprarodar

class LoginRequest(
    val email: String,
    val password: String
)

class RegisterRequest(
    val email: String,
    val password: String,
    val confirmPassword: String
)

const val EMAIL_VALID = "test@teste.com"
const val EMAIL_INVALID = "test@teste"
const val PASSWORD_VALID = "123456"
const val PASSWORD_INVALID = "123"

val loginRequestValid = LoginRequest(
    email = EMAIL_VALID,
    password = PASSWORD_VALID
)

val loginRequestWithInvalidEmail = LoginRequest(
    email = EMAIL_INVALID,
    password = PASSWORD_VALID
)

val loginRequestWithInvalidPassword = LoginRequest(
    email = EMAIL_VALID,
    password = PASSWORD_INVALID
)

val loginRequestWithInvalidConfirmPassword = LoginRequest(
    email = EMAIL_VALID,
    password = PASSWORD_VALID
)

val registerRequestValid = RegisterRequest(
    email = EMAIL_VALID,
    password = PASSWORD_VALID,
    confirmPassword = PASSWORD_VALID
)

val registerRequestWithInvalidEmail = RegisterRequest(
    email = EMAIL_INVALID,
    password = PASSWORD_VALID,
    confirmPassword = PASSWORD_VALID
)

val registerRequestWithInvalidPassword = RegisterRequest(
    email = EMAIL_VALID,
    password = PASSWORD_INVALID,
    confirmPassword = PASSWORD_VALID
)

val registerRequestWithInvalidConfirmPassword = RegisterRequest(
    email = EMAIL_VALID,
    password = PASSWORD_VALID,
    confirmPassword = PASSWORD_INVALID
)