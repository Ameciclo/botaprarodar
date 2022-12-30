package app.igormatos.botaprarodar.presentation.login.signin

import app.igormatos.botaprarodar.common.extensions.isValidEmail
import app.igormatos.botaprarodar.common.extensions.isValidPassword

data class SignInData(
    val email: String = "",
    val password: String = "",
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val showPassword: Boolean = false,
) {
    fun isSignInButtonEnable() = email.isValidEmail() && password.isValidPassword()
}
