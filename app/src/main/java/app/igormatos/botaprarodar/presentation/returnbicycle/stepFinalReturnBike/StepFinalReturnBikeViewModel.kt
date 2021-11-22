package app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.common.extensions.getLastWithdraw
import app.igormatos.botaprarodar.common.utils.formattedDate
import app.igormatos.botaprarodar.data.local.quiz.BikeDevolutionQuizBuilder
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.StepFinalReturnBikeUseCase
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.launch
import java.util.*

class StepFinalReturnBikeViewModel(
    private val bikeHolder: BikeHolder,
    val quizBuilder: BikeDevolutionQuizBuilder,
    val stepFinalUseCase: StepFinalReturnBikeUseCase,
    val devolutionStepper: ReturnStepper
) : ViewModel() {

    private val _state = MutableLiveData<UiState>()
    val state: LiveData<UiState>
        get() = _state

    private val _restartDevolutionFlow = MutableLiveData<Boolean>()
    val restartDevolutionFlow: LiveData<Boolean>
        get() = _restartDevolutionFlow

    private val date = Calendar.getInstance().time
    val devolution = formattedDate("dd MMM yyyy").format(date).replace(" ", " de ")
    private val devolutionDate = formattedDate().format(date)

    fun getBikeHolder() = bikeHolder.bike

    val withdrawDate
        get() =
            formattedDate().parse(getBikeHolder()?.getLastWithdraw()?.date).let { baseDate ->
                formattedDate("dd MMM yyyy").format(baseDate).replace(" ", " de ")
            }

    fun finalizeDevolution() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            val response = stepFinalUseCase.addDevolution(
                devolutionDate,
                bikeHolder,
                quizBuilder
            )
            when (response) {
                is SimpleResult.Success -> {
                    _state.postValue(UiState.Success)
                }
                is SimpleResult.Error -> {
                    _state.postValue(UiState.Error(DEFAULT_WITHDRAW_ERROR_MESSAGE))
                }
            }
        }

    }

    fun restartDevolution() {
        devolutionStepper.navigateToInitialStep()
        _restartDevolutionFlow.value = true
    }
}