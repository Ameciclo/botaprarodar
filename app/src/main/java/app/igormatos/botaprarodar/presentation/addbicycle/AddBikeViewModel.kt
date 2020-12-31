package app.igormatos.botaprarodar.presentation.addbicycle

import androidx.lifecycle.*
import app.igormatos.botaprarodar.common.NetworkResource
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

    private val registeredBicycleResult = MutableLiveData<NetworkResource<Boolean>>()

    fun getRegisteredBicycleResult(): LiveData<NetworkResource<Boolean>> = registeredBicycleResult

    fun registerBicycle(bicycle: Bicycle) {
        registeredBicycleResult.postValue(NetworkResource.Loading(null))
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
        registeredBicycleResult.postValue(NetworkResource.Error(e.message))
    }

    private fun resultSuccess() {
        registeredBicycleResult.postValue(NetworkResource.Success(true))
    }
}