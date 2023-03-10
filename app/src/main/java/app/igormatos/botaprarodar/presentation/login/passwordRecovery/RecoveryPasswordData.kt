package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import app.igormatos.botaprarodar.common.extensions.isValidEmail

data class RecoveryPasswordData(
    val email: String = "",
    val emailError: Int? = null,
) {
    fun isRecoverButtonEnable() = email.isValidEmail()
}