package app.igormatos.botaprarodar.presentation.login.registration

import androidx.lifecycle.*
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.presentation.login.signin.BprResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerUseCase: RegisterUseCase) : ViewModel() {

    val state: StateFlow<RegisterState>
        get() = _state.asStateFlow()

    private val _state: MutableStateFlow<RegisterState> =
        MutableStateFlow(RegisterState.Empty)

    fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val data = _state.value.data

            _state.value = RegisterState.Loading(data)

            when(val result = registerUseCase.invoke(data.email, data.password)) {
                is BprResult.Success -> onSuccess()
                is BprResult.Failure -> {
                    when (result.error) {
                        BprErrorType.NETWORK -> _state.value = RegisterState.Error(data, R.string.network_error_message)
                        BprErrorType.INVALID_ACCOUNT -> {
                            val newData = data.copy(emailError = R.string.email_already_registered_error)
                            _state.value =  RegisterState.Success(newData)
                        }
                        else -> _state.value = RegisterState.Error(data, R.string.unkown_error)
                    }
                }
            }
        }
    }

    fun onEmailChanged(text: String) {
        val data = _state.value.data.copy(email = text.trim(), emailError = null)
        _state.value = RegisterState.Success(data)
    }

    fun onPasswordChange(text: String) {
        val data = _state.value.data.copy(password = text.trim())
        _state.value = RegisterState.Success(data)
    }

    fun onConfirmPasswordChange(text: String) {
        val data = _state.value.data.copy(confirmPassword = text.trim())
        _state.value = RegisterState.Success(data)
    }

    fun onShowPasswordChanged(value: Boolean) {
        val data = _state.value.data.copy(showPassword = value)
        _state.value = RegisterState.Success(data)
    }
}