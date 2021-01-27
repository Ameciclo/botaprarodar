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

    var editMode = false
    var bike = Bike()

    val serialNumber = MutableLiveData("")
    val bikeName = MutableLiveData("")
    val orderNumber = MutableLiveData("")
    val imagePath = MutableLiveData("")

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

    fun updateBikeValues(bike: Bike) {
        bike.apply {
            serialNumber.value = this.serial_number
            bikeName.value = this.name
            orderNumber.value = this.order_number.toString()
            imagePath.value = this.photo_path
        }
        this.bike = bike
        editMode = true
    }

    private fun validateForm() {
        valid.value = isTextValid(imagePath.value) &&
                isTextValid(serialNumber.value) &&
                isTextValid(bikeName.value) &&
                isTextValid(orderNumber.value)
    }

    fun isTextValid(data: String?) = !data.isNullOrBlank()

    fun updateImagePath(imagePath: String) {
        this.imagePath.value = imagePath
    }

    fun registerBicycle() {
        val bike = getNewBike()
        _state.postValue(BikeFormStatus.Loading)

        if (editMode.not()) {
            viewModelScope.launch {
                addNewBikeUseCase.addNewBike(communityId = community.id, bike = bike).let {
                    when (it) {
                        is SimpleResult.Success -> resultSuccess(it.data)
                        is SimpleResult.Error -> resultError(it.exception)
                    }
                }
            }
        } else {
            viewModelScope.launch {
                addNewBikeUseCase.updateBike(communityId = community.id, bike = bike).let {
                    when (it) {
                        is SimpleResult.Success -> resultSuccess(it.data)
                        is SimpleResult.Error -> resultError(it.exception)
                    }
                }
            }
        }
    }

    private fun getNewBike(): Bike {
        return bike.apply {
            name = bikeName.value
            serial_number = serialNumber.value
            order_number = orderNumber.value?.toLong()
            path = imagePath.value.orEmpty()
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