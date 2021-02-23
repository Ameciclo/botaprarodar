package app.igormatos.botaprarodar.utils

import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.flow.flowOf

val bike = Bike(name = "Caloi")

val exception = Exception()

val listBikes = mutableListOf(bike)

val flowSuccess = flowOf(SimpleResult.Success(listBikes))

val flowError = flowOf(SimpleResult.Error(exception))