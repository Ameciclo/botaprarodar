package app.igormatos.botaprarodar.utils

import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.login.signin.SignInData

class LoginRequest(
    val email: String,
    val password: String,
    val confirmPassword: String
)

const val EMAIL_VALID = "test@teste.com"
const val EMAIL_TRAILING_SPACES_VALID = "   test@teste.com  "
const val EMAIL_INVALID = "test@teste"
const val PASSWORD_VALID = "123456"
const val PASSWORD_INVALID = "123"

val loginRequestValid = LoginRequest(
    email = EMAIL_VALID,
    password = PASSWORD_VALID,
    confirmPassword = PASSWORD_VALID
)

val loginEmailTrailingSpacesRequestValid = LoginRequest(
    email = EMAIL_TRAILING_SPACES_VALID,
    password = PASSWORD_VALID,
    confirmPassword = PASSWORD_VALID
)

val loginRequestWithInvalidEmail = LoginRequest(
    email = EMAIL_INVALID,
    password = PASSWORD_VALID,
    confirmPassword = PASSWORD_VALID
)

val loginRequestWithInvalidPassword = LoginRequest(
    email = EMAIL_VALID,
    password = PASSWORD_INVALID,
    confirmPassword = PASSWORD_VALID
)

val loginRequestWithInvalidConfirmPassword = LoginRequest(
    email = EMAIL_VALID,
    password = PASSWORD_VALID,
    confirmPassword = PASSWORD_INVALID
)

val signInDataValid = SignInData(
    email = EMAIL_VALID,
    password = PASSWORD_VALID
)

val signInDataInvalidAccount = SignInData(
    emailError = R.string.sign_in_incorrect_email_password_error,
    passwordError = R.string.sign_in_incorrect_email_password_error
)
