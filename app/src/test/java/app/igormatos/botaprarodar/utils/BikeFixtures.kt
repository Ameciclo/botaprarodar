package app.igormatos.botaprarodar.utils

import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.flow.flowOf
import java.util.*

val bike = Bike(name = "Caloi")

val exception = Exception()

val listBikes = mutableListOf(bike)

val flowSuccess = flowOf(SimpleResult.Success(listBikes))

val flowError = flowOf(SimpleResult.Error(exception))

val addDataResponseBike = AddDataResponse("New Bicycle")

val addDataResponseEditBike = AddDataResponse("Bicycle Edited")

val mapOfBikes = mapOf(
    Pair("123", Bike()),
    Pair("456", Bike()),
    Pair("789", Bike()),
    Pair("098", Bike()),
    Pair("876", Bike())
)

val bicycleRequest = BicycleRequest(
    id = "",
    available = true,
    inUse = false,
    name = "New Bicycle",
    orderNumber = 1010,
    serialNumber = "New Serial",
    createdDate = Date().toString()
)

val bikeSimpleSuccess = SimpleResult.Success(addDataResponseBike)

val bikeSimpleError = SimpleResult.Error(Exception())

val bikeSimpleSuccessEdit = SimpleResult.Success(addDataResponseEditBike)