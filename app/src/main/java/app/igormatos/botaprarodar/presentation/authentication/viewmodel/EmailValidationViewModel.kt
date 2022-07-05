package app.igormatos.botaprarodar.presentation.authentication.viewmodel

import androidx.lifecycle.*
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.authentication.Validator
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.EmailValidationState.*
import kotlinx.coroutines.launch

class EmailValidationViewModel(
    private val adminRepository: AdminRepository,
    private val emailValidator: Validator<String?>
) : ViewModel() {
    private val _viewState = MutableLiveData<EmailValidationState>()
    val viewState: LiveData<EmailValidationState>
        get() = _viewState

    val progressBarVisible: LiveData<Boolean>
        get() = Transformations.map(viewState) {
            it is SendLoading
        }

    val emailField = MutableLiveData<String>().apply {
        if (viewState.value is InitialState) {
            this.value = ""
        }
    }

    val nextButtonEnabled: LiveData<Boolean>
        get() = Transformations.map(emailField) {
            emailValidator.validate(it)
        }

    init {
        _viewState.value = InitialState
    }

    fun sendForm() {
        viewModelScope.launch {
            _viewState.value = SendLoading
            try {
                emailField.value?.let {
                    if (emailValidator.validate(it)) {
                        val isRegistered = adminRepository.isAdminRegistered(it.trim())
                        _viewState.value = SendSuccess(isAdminRegisted = isRegistered)
                    }
                }
            } catch (e: UserAdminErrorException.AdminNetwork) {
                _viewState.value = SendError(type = BprErrorType.NETWORK)
            } catch (e: Exception) {
                _viewState.value = SendError(type = BprErrorType.UNKNOWN)
            }finally {
                _viewState.postValue(Completed)
            }
        }
    }
}
