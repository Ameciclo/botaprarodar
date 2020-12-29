package app.igormatos.botaprarodar.presentation.addbicycle

import androidx.lifecycle.*
import app.igormatos.botaprarodar.domain.model.Bicycle
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.bicycle.AddNewBicycleUseCase
import app.igormatos.botaprarodar.presentation.STATUS
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddBikeViewModel(
    private val addNewBicycleUseCase: AddNewBicycleUseCase,
    private val community: Community
) : ViewModel(),
    LifecycleObserver {

    private val bicycleData: MutableLiveData<BicycleResponseView> =
        MutableLiveData(BicycleResponseView(status = null, errorMessage = null))

    fun getBicycleData(): LiveData<BicycleResponseView> = bicycleData


    /*fun addBicycle(bicycle: Bicycle) {
        _addBicycle(bicycle)
    }*/

     fun addBicycle(bicycle: Bicycle) {
        viewModelScope.launch {
           val result = addNewBicycleUseCase.addNewBicycle(communityId = community.id, bicycle = bicycle)

            when (result){
                is SimpleResult.Success -> {
                    bicycleData.postValue(bicycleData.value?.apply {
                        status = STATUS.SUCCESS
                    })
                }
                is SimpleResult.Error -> {
                    bicycleData.postValue(bicycleData.value?.apply {
                        status = STATUS.ERROR
                    })
                }
            }
        }
    }
}