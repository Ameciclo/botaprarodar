package app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.domain.UserHolder
import app.igormatos.botaprarodar.domain.adapter.WithdrawStepper
import app.igormatos.botaprarodar.domain.usecase.withdraw.SendBikeWithdraw
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import kotlinx.coroutines.launch
import java.lang.Exception

class BikeConfirmationViewModel(
    private val bikeHolder: BikeHolder,
    private val userHolder: UserHolder,
    private val sendBikeWithdraw: SendBikeWithdraw,
    private val withdrawStepper: WithdrawStepper
) : ViewModel() {

    private val _uiState = MutableLiveData<BikeWithdrawUiState>()
    val uiState: LiveData<BikeWithdrawUiState>
        get() = _uiState

    val bikeImageUrl = MutableLiveData<String>()
    val userImageUrl = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val bikeOrderNumber = MutableLiveData<String>()
    val bikeSeriesNumber = MutableLiveData<String>()
    val bikeName = MutableLiveData<String>()
    val confirmButtonEnabled: LiveData<Boolean>
        get() = Transformations.map(uiState) {
            it !is BikeWithdrawUiState.Loading &&
                    it !is BikeWithdrawUiState.Success
        }

    init {
        bikeImageUrl.value = bikeHolder.bike?.photoPath
        userImageUrl.value = userHolder.user?.profilePicture ?: ""
        userName.value = userHolder.user?.name ?: ""
        bikeOrderNumber.value = bikeHolder.bike?.orderNumber?.toString() ?: ""
        bikeSeriesNumber.value = bikeHolder.bike?.serialNumber ?: ""
        bikeName.value = bikeHolder.bike?.name ?: ""
    }

    fun confirmBikeWithdraw() {
        _uiState.value = BikeWithdrawUiState.Loading

        viewModelScope.launch {
            try {
                sendBikeWithdraw.execute(bikeHolder, userHolder)
                _uiState.postValue(BikeWithdrawUiState.Success)
            } catch (e: Exception) {
                _uiState.postValue(BikeWithdrawUiState.Error(DEFAULT_WITHDRAW_ERROR_MESSAGE))
            }
        }
    }

    fun restartWithdraw() {
        withdrawStepper.navigateToInitialStep()
    }
}