package app.igormatos.botaprarodar.presentation.login.signin

import app.igormatos.botaprarodar.common.enumType.BprErrorType

sealed class BprResult<out  T> {
    data class Success<T>(val data: T) : BprResult<T>()
    data class Failure(val exception: Exception? = null, val error: BprErrorType) : BprResult<Nothing>()
}

