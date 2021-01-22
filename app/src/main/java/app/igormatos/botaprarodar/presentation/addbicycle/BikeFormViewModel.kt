package app.igormatos.botaprarodar.presentation.addbicycle

import androidx.lifecycle.*
import app.igormatos.botaprarodar.common.BikeFormStatus
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.bicycle.AddNewBikeUseCase
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.launch
import java.lang.Exception

class BikeFormViewModel(
    private val addNewBikeUseCase: AddNewBikeUseCase,
    private val community: Community
) : BprViewModel<BikeFormStatus>() {
    private val UNKNOWN_ERROR = "Falha ao cadastrar a bicicleta"


    val serialNumber = MutableLiveData<String>("")
    val bikeName = MutableLiveData<String>("")
    val orderNumber = MutableLiveData<String>("")
    val imagePath = MutableLiveData<String>("")

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
        val bike = fillBike()
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

    private fun fillBike(): Bike {
        return Bike().apply {
            name = bikeName.value
            serial_number = this@BikeFormViewModel.serialNumber.value
            order_number = this@BikeFormViewModel.orderNumber.value?.toLong()
        }
    }

    private fun resultError(e: Exception) {
        _state.postValue(BikeFormStatus.Error(e.message ?: UNKNOWN_ERROR))
    }

    private fun resultSuccess(bikeName: String) {
        _state.postValue(BikeFormStatus.Success(bikeName))
    }
}