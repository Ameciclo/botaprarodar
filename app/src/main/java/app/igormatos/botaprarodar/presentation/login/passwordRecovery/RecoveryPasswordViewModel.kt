package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import androidx.lifecycle.*
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BprErrorType.INVALID_ACCOUNT
import app.igormatos.botaprarodar.common.enumType.BprErrorType.NETWORK
import app.igormatos.botaprarodar.presentation.login.signin.BprResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecoveryPasswordViewModel(private val useCase: PasswordRecoveryUseCase) : ViewModel() {

    val state: StateFlow<RecoveryPasswordState>
        get() = _state.asStateFlow()

    private val _state: MutableStateFlow<RecoveryPasswordState> =
        MutableStateFlow(RecoveryPasswordState.Success(RecoveryPasswordData()))

    fun onEmailChanged(text: String) {
        val data = _state.value.data.copy(email = text.trim(), emailError = null)
        _state.value = RecoveryPasswordState.Success(data)
    }

    fun recoverPassword(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val data = _state.value.data
            _state.value = RecoveryPasswordState.Loading(data)

            when (val result = useCase.invoke(data.email)) {
                is BprResult.Success -> onSuccess()
                is BprResult.Failure -> {
                    _state.value = when (result.error) {
                        NETWORK -> RecoveryPasswordState.Error(data, R.string.network_error_message)
                        INVALID_ACCOUNT -> {
                            val newData = data.copy(emailError = R.string.sign_in_email_error)
                            RecoveryPasswordState.Success(newData)
                        }
                        else -> RecoveryPasswordState.Error(data, R.string.unkown_error)
                    }
                }
            }
        }
    }
}