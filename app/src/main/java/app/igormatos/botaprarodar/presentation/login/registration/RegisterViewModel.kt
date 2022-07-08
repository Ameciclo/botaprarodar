package app.igormatos.botaprarodar.presentation.login.registration

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    val isButtonRegisterEnable = MediatorLiveData<Boolean>().apply {
        addSource(email) { validateForm() }
        addSource(password) { validateForm() }
        addSource(confirmPassword) { validateForm() }
    }

    private val _registerState = MutableLiveData<RegisterState>()
    val registerState: LiveData<RegisterState>
        get() = _registerState

    private fun validateForm() {
        isButtonRegisterEnable.value = registerUseCase.isRegisterFormValid(
            email = email.value?.trim(),
            password = password.value,
            confirmPassword = confirmPassword.value
        )
    }

    fun register() {
        _registerState.value = RegisterState.Loading
        viewModelScope.launch {
            _registerState.value = registerUseCase.register(
                email = email.value.toString().trim(),
                password = password.value.toString()
            )
        }
    }
}