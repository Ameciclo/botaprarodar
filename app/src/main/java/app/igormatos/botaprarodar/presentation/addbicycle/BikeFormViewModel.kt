package app.igormatos.botaprarodar.presentation.addbicycle

import androidx.lifecycle.*
import app.igormatos.botaprarodar.common.BikeFormStatus
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.bicycle.AddNewBikeUseCase
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.launch

class BikeFormViewModel(
    private val addNewBikeUseCase: AddNewBikeUseCase,
    private val community: Community
) : BprViewModel<BikeFormStatus>() {

    private val _serialNumber = MutableLiveData("")
    val serialNumber:LiveData<String> = _serialNumber
    private val _bikeName = MutableLiveData("")
    val bikeName: LiveData<String> = _bikeName
    private val _orderNumber = MutableLiveData("")
    val orderNumber: LiveData<String> = _orderNumber
    private val _imagePath = MutableLiveData("")
    val imagePath: LiveData<String> = _imagePath

    val valid = MediatorLiveData<Boolean>().apply {
        addSource(_imagePath) {
            validateForm()
        }
        addSource(_serialNumber) {
            validateForm()
        }
        addSource(_bikeName) {
            validateForm()
        }
        addSource(_orderNumber) {
            validateForm()
        }
    }

    private fun validateForm() {
        valid.value = isTextValid(_imagePath.value) &&
                isTextValid(_serialNumber.value) &&
                isTextValid(_bikeName.value) &&
                isTextValid(_orderNumber.value)
    }

    fun isTextValid(data: String?) = !data.isNullOrBlank()

    fun updateImagePath(imagePath: String) {
        this._imagePath.value = imagePath
    }

    fun registerBicycle() {
        val bike = getNewBike()
        _state.postValue(BikeFormStatus.Loading)

        viewModelScope.launch {
            addNewBikeUseCase.addNewBike(communityId = community.id, bike = bike).let {
                when (it) {
                    is SimpleResult.Success -> resultSuccess(it.data)
                    is SimpleResult.Error -> resultError(it.exception)
                }
            }
        }
    }

    private fun getNewBike(): Bike {
        return Bike().apply {
            name = _bikeName.value
            serial_number = this@BikeFormViewModel._serialNumber.value
            order_number = this@BikeFormViewModel._orderNumber.value?.toLong()
            path = this@BikeFormViewModel._imagePath.value.orEmpty()
        }
    }

    private fun resultError(e: Exception) {
        _state.postValue(BikeFormStatus.Error(e.message ?: UNKNOWN_ERROR))
    }

    private fun resultSuccess(bikeName: String) {
        _state.postValue(BikeFormStatus.Success(bikeName))
    }

    companion object {
        private const val UNKNOWN_ERROR = "Falha ao cadastrar a bicicleta"
    }
}