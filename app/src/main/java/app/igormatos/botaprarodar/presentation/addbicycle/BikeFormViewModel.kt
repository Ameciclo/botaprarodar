package app.igormatos.botaprarodar.presentation.addbicycle

import androidx.lifecycle.*
import app.igormatos.botaprarodar.common.Status
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.bicycle.AddNewBikeUseCase
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.launch
import java.lang.Exception

class BikeFormViewModel(
    private val addNewBikeUseCase: AddNewBikeUseCase,
    private val community: Community
) : ViewModel(),
    LifecycleObserver {

    private val registeredBicycleResult = MutableLiveData<Status<String>>()

    var loadingStatus = Status.Loading("")
    var successStatus = Status.Success("")

    fun getRegisteredBicycleResult(): LiveData<Status<String>> = registeredBicycleResult

    fun registerBicycle(bike: Bike) {
        registeredBicycleResult.postValue(loadingStatus)

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
        registeredBicycleResult.postValue(Status.Error(e.message))
    }

    private fun resultSuccess(bikeName: String) {
        successStatus = Status.Success(bikeName)
        registeredBicycleResult.postValue(successStatus)
    }
}