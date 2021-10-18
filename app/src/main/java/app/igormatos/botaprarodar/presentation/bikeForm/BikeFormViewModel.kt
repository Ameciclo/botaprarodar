package app.igormatos.botaprarodar.presentation.bikeForm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.BikeFormStatus
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.bikeForm.BikeFormUseCase
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.launch

class BikeFormViewModel(
    private val bikeFormUseCase: BikeFormUseCase,
    private val community: Community,
    private val communityBikesSerialNumbers: ArrayList<String>
) : BprViewModel<BikeFormStatus>() {

    var isEditModeAvailable = false
    var bike = Bike()

    val serialNumber = MutableLiveData("")
    val bikeName = MutableLiveData("")
    val orderNumber = MutableLiveData("")
    val imagePath = MutableLiveData("")

    val serialNumberErrorValidationMap = MediatorLiveData<MutableMap<Int, Boolean>>().apply {
        value = mutableMapOf()

        addSource(serialNumber) {
            validateSerialNumber()
        }
    }

    val valid = MediatorLiveData<Boolean>().apply {
        addSource(imagePath) {
            validateForm()
        }
        addSource(serialNumber) {
            validateForm()
        }
        addSource(bikeName) {
            validateForm()
        }
        addSource(orderNumber) {
            validateForm()
        }
    }

    private fun validateSerialNumber() {
        with(serialNumberErrorValidationMap.value) {
            this?.set(
                SERIAL_NUMBER_INVALID_ERROR,
                !isTextValid(serialNumber.value)
            )

            this?.set(
                SERIAL_NUMBER_ALREADY_REGISTERED_ERROR,
                communityBikesHasSerialNumber(serialNumber.value)
            )
        }
    }

    private fun communityBikesHasSerialNumber(serialNumber: String?) =
        communityBikesSerialNumbers.any { it.equals(serialNumber) }

    private fun validateForm() {
        valid.value = isTextValid(imagePath.value) &&
                isTextValid(bikeName.value) &&
                isTextValid(orderNumber.value) &&
                isSerialNumberValid()
    }

    internal fun isTextValid(data: String?) = !data.isNullOrBlank()

    private fun isSerialNumberValid(): Boolean {
        val existsSerialNumberError = serialNumberErrorValidationMap.value?.containsValue(true)
        if (existsSerialNumberError != null) {
            return !existsSerialNumberError
        }
        return true
    }

    fun updateBikeValues(bike: Bike) {
        bike.apply {
            this@BikeFormViewModel.serialNumber.value = this.serialNumber
            this@BikeFormViewModel.bikeName.value = this.name
            this@BikeFormViewModel.orderNumber.value = this.orderNumber?.toString() ?: ""
            this@BikeFormViewModel.imagePath.value = this.photoPath
        }
        setBikeToEditMode(bike)
    }

    private fun setBikeToEditMode(bike: Bike) {
        if(bike != null) {
            this.bike = bike
            isEditModeAvailable = true
            communityBikesSerialNumbers.remove(bike.serialNumber)
        }
    }

    fun updateImagePath(imagePath: String) {
        this.imagePath.value = imagePath
    }

    fun saveBike() {
        val bike = getNewBike()
        _state.postValue(BikeFormStatus.Loading)
        viewModelScope.launch {
            if (isEditModeAvailable)
                updateBike(bike)
            else
                addNewBike(bike)
        }
    }

    private fun getNewBike(): Bike {
        return bike.apply {
            name = this@BikeFormViewModel.bikeName.value
            serialNumber = this@BikeFormViewModel.serialNumber.value
            orderNumber = this@BikeFormViewModel.orderNumber.value?.toLong()
            path = this@BikeFormViewModel.imagePath.value.orEmpty()
            communityId = community.id
        }
    }

    private suspend fun updateBike(bike: Bike) {
        bikeFormUseCase.startUpdateBike(bike = bike).let {
            when (it) {
                is SimpleResult.Success -> resultSuccess(it.data.name)
                is SimpleResult.Error -> resultError()
            }
        }
    }

    private suspend fun addNewBike(bike: Bike) {
        bikeFormUseCase.addNewBike(bike = bike).let {
            when (it) {
                is SimpleResult.Success -> resultSuccess(it.data.name)
                is SimpleResult.Error -> resultError()
            }
        }
    }

    private fun resultSuccess(bikeName: String) {
        _state.postValue(BikeFormStatus.Success(bikeName))
    }

    private fun resultError() {
        val message = if (isEditModeAvailable) UNKNOWN_ERROR_EDIT else UNKNOWN_ERROR_REGISTER
        _state.postValue(BikeFormStatus.Error(message))
    }

    companion object {
        private const val UNKNOWN_ERROR_REGISTER =
            R.string.error_registering_bicycle

        private const val UNKNOWN_ERROR_EDIT =
            R.string.error_editing_bicycle

        private const val SERIAL_NUMBER_ALREADY_REGISTERED_ERROR =
            R.string.bicycle_serial_number_already_registered

        private const val SERIAL_NUMBER_INVALID_ERROR =
            R.string.bicycle_serial_number_invalid
    }

}