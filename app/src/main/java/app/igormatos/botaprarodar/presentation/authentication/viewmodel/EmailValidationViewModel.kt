package app.igormatos.botaprarodar.presentation.authentication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EmailValidationViewModel : ViewModel() {
    private val _state = MutableLiveData<EmailValidationState>()
    val state: LiveData<EmailValidationState>
        get() = _state

    val progressBarEnabled: Boolean = state.value is EmailValidationState.Loading

}