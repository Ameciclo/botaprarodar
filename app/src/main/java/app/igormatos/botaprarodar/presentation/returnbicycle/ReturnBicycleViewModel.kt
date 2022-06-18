package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.common.utils.formattedDate
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.Quiz
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.StepFinalReturnBikeUseCase
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.StepOneReturnBikeUseCase
import app.igormatos.botaprarodar.domain.usecase.users.GetUserByIdUseCase
import app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike.DEFAULT_RETURNS_ERROR_MESSAGE
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalCoroutinesApi
class ReturnBicycleViewModel(
    val stepperAdapter: ReturnStepper,
    private val stepOneReturnBikeUseCase: StepOneReturnBikeUseCase,
    private val stepFinalReturnBikeUseCase: StepFinalReturnBikeUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : ViewModel() {
    private val _bikesAvailableToReturn = MutableLiveData<SimpleResult<List<Bike>>>()
    val bikesAvailableToReturn: LiveData<SimpleResult<List<Bike>>> = _bikesAvailableToReturn

    private val _bikesAvailable: MutableLiveData<List<Bike>?> = MutableLiveData<List<Bike>?>()
    val bikesAvailable: MutableLiveData<List<Bike>?> = _bikesAvailable

    private val _devolutionQuiz: MutableLiveData<Quiz> = MutableLiveData<Quiz>()
    val devolutionQuiz: LiveData<Quiz> = _devolutionQuiz

    private val _bikeHolder: MutableLiveData<Bike> = MutableLiveData<Bike>()
    val bikeHolder: LiveData<Bike> = _bikeHolder

    private val _userHolder: MutableLiveData<User> = MutableLiveData<User>()
    val userHolder: LiveData<User> = _userHolder

    private val _uiStep = MutableLiveData<StepConfigType>()
    val uiStep: LiveData<StepConfigType> = _uiStep

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> = _errorState

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

    fun getBikesInUseToReturn(communityId: String) {
        viewModelScope.launch {
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
        viewModelScope.launch {
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

    fun getUserBy(userId: String) = viewModelScope.launch {
        getUserByIdUseCase.execute(userId)?.let { user ->
            _userHolder.value = user
        }
    }
}