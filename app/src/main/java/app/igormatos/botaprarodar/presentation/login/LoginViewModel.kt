package app.igormatos.botaprarodar.presentation.login

import androidx.lifecycle.*
import app.igormatos.botaprarodar.presentation.login.resendEmail.ResendEmailState
import app.igormatos.botaprarodar.presentation.login.resendEmail.ResendEmailUseCase
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val resendEmailUseCase: ResendEmailUseCase
) : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val isButtonLoginEnable = MediatorLiveData<Boolean>().apply {
        addSource(email) { validateLoginForm() }
        addSource(password) { validateLoginForm() }
    }

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState>
        get() = _loginState

    private val _resendEmailState = MutableLiveData<ResendEmailState>()
    val resendEmailState: LiveData<ResendEmailState>
        get() = _resendEmailState

    private fun validateLoginForm() {
        isButtonLoginEnable.value = loginUseCase.isLoginFormValid(
            email = email.value,
            password = password.value,
        )
    }

    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            _loginState.value = loginUseCase.authenticateAdmin(email, password)
        }
    }

    fun sendEmailVerification() {
        _resendEmailState.value = ResendEmailState.Loading
        viewModelScope.launch {
            _resendEmailState.value = resendEmailUseCase.execute()
        }
    }
}