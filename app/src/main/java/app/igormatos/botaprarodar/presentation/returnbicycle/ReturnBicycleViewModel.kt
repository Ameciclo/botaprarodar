package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.lifecycle.*
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.common.utils.formattedDate
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.Quiz
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.GetNeighborhoodsUseCase
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.StepFinalReturnBikeUseCase
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.StepOneReturnBikeUseCase
import app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike.DEFAULT_RETURNS_ERROR_MESSAGE
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalCoroutinesApi
class ReturnBicycleViewModel(
    val stepperAdapter: ReturnStepper,
    private val stepOneReturnBikeUseCase: StepOneReturnBikeUseCase,
    private val stepFinalReturnBikeUseCase: StepFinalReturnBikeUseCase,
    private val preferencesModule: SharedPreferencesModule,
    private val neighborhoodsUseCase: GetNeighborhoodsUseCase,
) : ViewModel(), DefaultLifecycleObserver {
    private val _bikesAvailableToReturn = MutableLiveData<SimpleResult<List<Bike>>>()
    val bikesAvailableToReturn: LiveData<SimpleResult<List<Bike>>> = _bikesAvailableToReturn

    private val _bikesAvailable: MutableLiveData<List<Bike>?> = MutableLiveData<List<Bike>?>()
    val bikesAvailable: MutableLiveData<List<Bike>?> = _bikesAvailable

    private val _devolutionQuiz: MutableLiveData<Quiz> = MutableLiveData<Quiz>()
    val devolutionQuiz: LiveData<Quiz> = _devolutionQuiz

    private val _bikeHolder: MutableLiveData<Bike> = MutableLiveData<Bike>()
    val bikeHolder: LiveData<Bike> = _bikeHolder

    private val _uiStep = MutableLiveData<StepConfigType>()
    val uiStep: LiveData<StepConfigType> = _uiStep

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> = _errorState

    private val _neighborhoods = MutableLiveData<Array<String>>()
    val neighborhoods: LiveData<Array<String>> = _neighborhoods

    init {
        viewModelScope.launch {
            _neighborhoods.postValue(neighborhoodsUseCase.invoke().toTypedArray())
        }
    }

    fun setInitialStep() {
        stepperAdapter.setCurrentStep(StepConfigType.SELECT_BIKE)
        _uiStep.postValue(stepperAdapter.currentStep.value)
    }

    fun navigateToNextStep() {
        stepperAdapter.navigateToNext()
        _uiStep.postValue(stepperAdapter.currentStep.value)
    }

    fun navigateToPrevious() {
        stepperAdapter.navigateToPrevious()
        _uiStep.postValue(stepperAdapter.currentStep.value)
    }

    fun navigateToFinishedStep() {
        stepperAdapter.setCurrentStep(StepConfigType.FINISHED_ACTION)
        _uiStep.postValue(stepperAdapter.currentStep.value)
    }

    override fun onStart(owner: LifecycleOwner) {
        setInitialStep()
        val communityId = preferencesModule.getJoinedCommunity().id
        _bikesAvailable.value = emptyList()
        getBikesInUseToReturn(communityId)
    }

    fun getBikesInUseToReturn(communityId: String) {
        viewModelScope.launch(NonCancellable) {
            val value = stepOneReturnBikeUseCase.getBikesInUseToReturn(communityId = communityId)
            _bikesAvailableToReturn.value = value
            when (value) {
                is SimpleResult.Success -> {
                    _bikesAvailable.value = value.data
                }
                is SimpleResult.Error -> {
                    value.exception
                }
            }
        }
    }

    fun addDevolution(onFinished: () -> Unit) {
        viewModelScope.launch(NonCancellable) {
            _loadingState.postValue(true)
            val response = stepFinalReturnBikeUseCase.addDevolution(
                getCurrentTime(),
                bikeHolder.value!!,
                devolutionQuiz.value!!
            )
            when (response) {
                is SimpleResult.Success -> {
                    onFinished()
                }
                is SimpleResult.Error -> {
                    _errorState.postValue(DEFAULT_RETURNS_ERROR_MESSAGE)
                }
            }
            _loadingState.postValue(false)
        }
    }

    private fun getCurrentTime(): String {
        return formattedDate().format(Calendar.getInstance(Locale("pt", "BR")).time)
    }

    fun setQuiz(quiz: Quiz) {
        _devolutionQuiz.value = quiz
    }

    fun setBike(bike: Bike) {
        _bikeHolder.value = bike
    }
    
    fun loadBicycleReturnUseArray(): Array<String> = stepOneReturnBikeUseCase.getBicycleReturnUseMap().values.toTypedArray()
}
