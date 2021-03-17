package app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.common.extensions.getLastWithdraw
import app.igormatos.botaprarodar.data.local.quiz.BikeDevolutionQuizBuilder
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.StepFinalReturnBikeUseCase
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class StepFinalReturnBikeViewModel(
    private val bikeHolder: BikeHolder,
    val quizBuilder: BikeDevolutionQuizBuilder,
    val stepFinalUseCase: StepFinalReturnBikeUseCase
) : ViewModel() {

    private val _state = MutableLiveData<SimpleResult<AddDataResponse>>()
    val state: LiveData<SimpleResult<AddDataResponse>>
        get() = _state

    private val date = Calendar.getInstance().time
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val devolutionDate = dateFormat.format(date)

    fun getBikeHolder() = bikeHolder.bike

    fun finalizeDevolution() {
        viewModelScope.launch {
            val response = stepFinalUseCase.addDevolution(
                devolutionDate,
                bikeHolder,
                quizBuilder
            )
            _state.postValue(response)
        }

    }
}