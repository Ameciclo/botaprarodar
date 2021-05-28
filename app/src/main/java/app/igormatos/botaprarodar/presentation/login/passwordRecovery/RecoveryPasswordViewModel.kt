package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class RecoveryPasswordViewModel(
    private val passwordRecoveryUseCase: PasswordRecoveryUseCase
) : ViewModel() {


    val email = MutableLiveData<String>()

    val isButtonEnable = MediatorLiveData<Boolean>().apply {
        addSource(email) { validateForm() }
    }

    private val _passwordRecoveryState = MutableLiveData<PasswordRecoveryState>()
    val passwordRecoveryState: LiveData<PasswordRecoveryState>
        get() = _passwordRecoveryState


    private fun validateForm() {
        isButtonEnable.value = passwordRecoveryUseCase.isEmailValid(
            email = email.value
        )
    }

    fun recoveryPassword(email: String) {
        _passwordRecoveryState.value = PasswordRecoveryState.Loading
        viewModelScope.launch {
            _passwordRecoveryState.value = passwordRecoveryUseCase.sendPasswordResetEmail(email)
        }
    }
}