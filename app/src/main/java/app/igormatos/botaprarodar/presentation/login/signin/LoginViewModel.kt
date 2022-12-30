package app.igormatos.botaprarodar.presentation.login.signin

import androidx.lifecycle.*
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BprErrorType.*
import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.presentation.login.resendEmail.ResendEmailUseCase
import app.igormatos.botaprarodar.domain.usecase.signin.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val resendEmailUseCase: ResendEmailUseCase
) : ViewModel() {

    val state : StateFlow<LoginState>
        get() = _state.asStateFlow()

    private val _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Success(SignInData()))

    fun onEmailChanged(text: String) {
        val data = _state.value.data.copy(email = text.trim(), emailError = null, passwordError = null)
        _state.value = LoginState.Success(data)
    }

    fun onPasswordChanged(text: String) {
        val data = _state.value.data.copy(password = text.trim(), emailError = null, passwordError = null)
        _state.value = LoginState.Success(data)
    }

    fun onShowPasswordChanged(checked: Boolean) {
        val data = state.value.data.copy(showPassword = checked)
        _state.value = LoginState.Success(data)
    }

    fun login(onSuccess: (Admin) -> Unit) {
        viewModelScope.launch {
            val data = _state.value.data
            _state.value = LoginState.Loading(data)

            when(val result = loginUseCase.invoke(data.email, data.password)) {
                is SignInResult.Success -> onSuccess(result.data)
                is SignInResult.Failure -> {
                    when(result.error) {
                        NETWORK -> _state.value = LoginState.Error(data, R.string.network_error_message)
                        UNKNOWN -> _state.value = LoginState.Error(data, R.string.login_error)
                        INVALID_PASSWORD, INVALID_ACCOUNT -> {
                            val newState = data.copy(
                                emailError = R.string.sign_in_incorrect_email_password_error,
                                passwordError = R.string.sign_in_incorrect_email_password_error
                            )

                            _state.value = LoginState.Success(newState)
                        }
                        EMAIL_NOT_VERIFIED -> _state.value = LoginState.RetryVerifyEmail(data)
                    }
                }
            }
        }
    }

    fun sendEmailVerification() {
        val data = _state.value.data
        _state.value = LoginState.Loading(data)

        viewModelScope.launch {
            when(val result = resendEmailUseCase.invoke()) {
                is SignInResult.Success -> _state.value = LoginState.EmailSent(data)
                is SignInResult.Failure -> {
                    val message = when(result.error) {
                        NETWORK -> R.string.network_error_message
                        else -> R.string.login_error
                    }

                    _state.value = LoginState.Error(data, message)
                }
            }
        }
    }
}