package app.igormatos.botaprarodar.presentation.login.signin

import app.igormatos.botaprarodar.common.enumType.BprErrorType

sealed class SignInResult<out  T> {
    data class Success<T>(val data: T) : SignInResult<T>()
    data class Failure(val exception: Exception, val error: BprErrorType) : SignInResult<Nothing>()
}

