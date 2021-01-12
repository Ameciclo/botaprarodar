package app.igormatos.botaprarodar.presentation.authentication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import kotlinx.coroutines.launch

class RegistrationViewModel(private val adminRepository: AdminRepository) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean>
        get() = _loading

    private val _registrationState = MutableLiveData<RegistrationState>()
    val registrationState : LiveData<RegistrationState>
        get() = _registrationState

    fun createAccount(email: String, password: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            try {
                adminRepository.createAdmin(email, password)
                _registrationState.postValue(RegistrationState.Success)
            } catch (e: UserAdminErrorException.AdminNetwork) {
                _registrationState.postValue(RegistrationState.Error)
            } catch (f: UserAdminErrorException.AdminInvalid) {
                _registrationState.postValue(RegistrationState.InvalidEmail)
            }
            _loading.postValue(false)
        }

    }

}