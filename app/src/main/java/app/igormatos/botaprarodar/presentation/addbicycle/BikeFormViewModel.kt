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
) : BprViewModel<BikeFormStatus>(){
    private val UNKNOWN_ERROR = "Falha ao cadastrar a bicicleta"

    fun registerBicycle(bike: Bike) {
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

    private fun resultError(e: Exception) {
        _state.postValue(BikeFormStatus.Error(e.message ?: UNKNOWN_ERROR))
    }

    private fun resultSuccess(bikeName: String) {
        _state.postValue(BikeFormStatus.Success(bikeName))
    }
}