package app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike

import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import app.igormatos.botaprarodar.presentation.returnbicycle.StepperAdapter
import java.text.SimpleDateFormat
import java.util.*

class StepFinalReturnBikeViewModel(
    val stepperAdapter: StepperAdapter.ReturnStepper,
    private val bikeHolder: BikeHolder
) : ViewModel() {

    fun finalizeDevolution() {}

    fun getBikeHolder() = bikeHolder.bike

    val date = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    val devolutionDate = dateFormat.format(date)
}