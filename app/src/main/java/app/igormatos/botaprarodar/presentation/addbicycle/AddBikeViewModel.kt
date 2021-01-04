package app.igormatos.botaprarodar.presentation.addbicycle

import androidx.lifecycle.*
import app.igormatos.botaprarodar.common.Status
import app.igormatos.botaprarodar.domain.model.Bicycle
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.bicycle.AddNewBicycleUseCase
import kotlinx.coroutines.launch
import java.lang.Exception

class AddBikeViewModel(
    private val addNewBicycleUseCase: AddNewBicycleUseCase,
    private val community: Community
) : ViewModel(),
    LifecycleObserver {

    private val registeredBicycleResult = MutableLiveData<Status<Boolean>>()

    fun getRegisteredBicycleResult(): LiveData<Status<Boolean>> = registeredBicycleResult

    fun registerBicycle(bicycle: Bicycle) {
        registeredBicycleResult.postValue(Status.Loading(null))

        viewModelScope.launch {
            try {
                val result = addNewBicycleUseCase.addNewBicycle(
                    communityId = community.id,
                    bicycle = bicycle)

                resultSuccess()

            } catch (e: Exception) {
                resultError(e)
            }
        }
    }

    private fun resultError(e: Exception) {
        registeredBicycleResult.postValue(Status.Error(e.message))
    }

    private fun resultSuccess() {
        registeredBicycleResult.postValue(Status.Success(true))
    }
}