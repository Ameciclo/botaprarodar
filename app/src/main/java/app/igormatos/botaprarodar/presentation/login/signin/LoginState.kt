package app.igormatos.botaprarodar.presentation.login.signin

sealed class LoginState(open val data: SignInData) {
    data class Success(override val data: SignInData) : LoginState(data)
    data class Error(override val data: SignInData, val message: Int) : LoginState(data)
    data class RetryVerifyEmail(override val data: SignInData) : LoginState(data)
    data class EmailSent(override val data: SignInData) : LoginState(data)
    data class Loading(override val data: SignInData) : LoginState(data)
}