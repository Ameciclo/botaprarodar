package app.igormatos.botaprarodar.presentation.login.registration

import androidx.annotation.StringRes

sealed class RegisterState(open val data: RegisterData) {
    object Empty : RegisterState(RegisterData())
    class Success(override val data: RegisterData) : RegisterState(data)
    class Loading(override val data: RegisterData) : RegisterState(data)
    class Error(
        override val data: RegisterData,
        @StringRes val message: Int
    ) : RegisterState(data)
}