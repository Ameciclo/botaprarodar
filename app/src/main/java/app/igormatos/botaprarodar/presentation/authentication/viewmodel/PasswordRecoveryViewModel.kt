package app.igormatos.botaprarodar.presentation.authentication.viewmodel

import androidx.lifecycle.*
import app.igormatos.botaprarodar.common.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.authentication.Validator
import kotlinx.coroutines.launch
import java.lang.Exception

class PasswordRecoveryViewModel(
    private val emailValidator: Validator<String>,
    private val adminRepository: AdminRepository
) : ViewModel() {
    private val _viewState = MutableLiveData<SendPasswordRecoveryViewState>()
    val viewState: LiveData<SendPasswordRecoveryViewState>
        get() = _viewState

    val usernameField = MutableLiveData<String>()

    val saveButtonEnabled: LiveData<Boolean>
        get() = Transformations.map(usernameField) {
            emailValidator.validate(it)
        }

    val isLoadingVisible: LiveData<Boolean>
    get() = Transformations.map(viewState){
        it is SendPasswordRecoveryViewState.SendLoading
    }

    fun sendPasswordResetEmail() {
        _viewState.value = SendPasswordRecoveryViewState.SendLoading
        viewModelScope.launch {
            usernameField.value?.let {
                try {
                    val result = adminRepository.sendPasswordResetEmail(it)
                    updateViewState(result)
                } catch (e: UserAdminErrorException.AdminNetwork) {
                    _viewState.value =
                        SendPasswordRecoveryViewState.SendError(type = BprErrorType.NETWORK)
                } catch (e: Exception) {
                    _viewState.value =
                        SendPasswordRecoveryViewState.SendError(type = BprErrorType.UNKNOWN)
                }
            }
        }
    }

    private fun updateViewState(isComplete: Boolean) {
        if (isComplete) {
            _viewState.value = SendPasswordRecoveryViewState.SendSuccess
        } else {
            _viewState.value =
                SendPasswordRecoveryViewState.SendError(type = BprErrorType.UNKNOWN)
        }
    }
}
