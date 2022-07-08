package app.igormatos.botaprarodar.presentation.authentication.viewmodel

import androidx.lifecycle.*
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.authentication.Validator
import kotlinx.coroutines.launch

class SignInViewModel(
    private val adminRepository: AdminRepository,
    private val passwordValidator: Validator<String?>
) : ViewModel() {
    private val _viewState = MutableLiveData<SignInViewState>()
    val viewState: LiveData<SignInViewState>
        get() = _viewState

    val progressBarVisible: LiveData<Boolean>
        get() = Transformations.map(viewState) {
            it is SignInViewState.SendLoading
        }

    val passwordField = MutableLiveData<String>()

    val signInButtonEnabled: LiveData<Boolean>
        get() = Transformations.map(passwordField) {
            passwordValidator.validate(it)
        }

    fun sendForm(email: String) {
        viewModelScope.launch {
            _viewState.value = SignInViewState.SendLoading
            try {
                passwordField.value?.let {
                    val result = adminRepository.authenticateAdmin(email.trim(), it)
                    _viewState.value = SignInViewState.SendSuccess(result)
                }
            } catch (e: UserAdminErrorException.AdminNetwork) {
                _viewState.value = SignInViewState.SendError(type = BprErrorType.NETWORK)
            } catch (e: UserAdminErrorException.AdminPasswordInvalid) {
                _viewState.value = SignInViewState.SendError(type = BprErrorType.INVALID_PASSWORD)
            } catch (e: Exception) {
                _viewState.value = SignInViewState.SendError(type = BprErrorType.UNKNOWN)
            }
        }
    }
}
