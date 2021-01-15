package app.igormatos.botaprarodar.presentation.authentication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.common.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.authentication.EmailValidator
import app.igormatos.botaprarodar.presentation.authentication.MIN_PASSWORD_LENGTH
import kotlinx.coroutines.launch
import java.lang.Exception

class RegistrationViewModel(private val adminRepository: AdminRepository) : ViewModel() {

    private val validator = EmailValidator()

    private val _registrationState = MutableLiveData<RegistrationState>()
    val registrationState : LiveData<RegistrationState>
        get() = _registrationState

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean>
        get() = _loading

    private val _emailValid = MutableLiveData<Boolean>()
    val emailValid : LiveData<Boolean>
        get() = _emailValid

    private val _passwordValid = MutableLiveData<Boolean>()
    val passwordValid : LiveData<Boolean>
        get() = _passwordValid

    fun createAccount(email: String, password: String) {
        if (areFieldsValid(email, password)) {
            viewModelScope.launch {
                _loading.value = true
                try {
                    adminRepository.createAdmin(email, password)
                    _registrationState.value = RegistrationState.SendSuccess
                } catch (e: UserAdminErrorException.AdminNetwork) {
                    _registrationState.value = RegistrationState.SendError(type = BprErrorType.NETWORK)
                } catch (e: Exception) {
                    _registrationState.value = RegistrationState.SendError(type = BprErrorType.UNKNOWN)
                } finally {
                    _loading.value = false
                    _registrationState.postValue(RegistrationState.Completed)
                }
            }
        }
    }

    private fun areFieldsValid(email: String, password: String) : Boolean {
        val emailValidator = isEmailValid(email)
        _emailValid.value = emailValidator
        val passwordValidator = isPasswordValid(password.length)
        _passwordValid.value = passwordValidator
        return passwordValidator && emailValidator
    }

    private fun isPasswordValid(textLength: Int?): Boolean {
        textLength?.let {
            return textLength >= MIN_PASSWORD_LENGTH
        }
        return false
    }


     private fun isEmailValid(email: String): Boolean =
        validator.validate(email)
}