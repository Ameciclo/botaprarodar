package app.igormatos.botaprarodar.presentation.login.registration

import app.igormatos.botaprarodar.common.extensions.isValidEmail
import app.igormatos.botaprarodar.common.extensions.isValidPassword

data class RegisterData(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val showPassword: Boolean = false,
    val emailError: Int? = null,
) {
    fun isButtonEnable() = email.isValidEmail() && password.isValidPassword()
            && password == confirmPassword
}