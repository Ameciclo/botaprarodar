package app.igormatos.botaprarodar.utils

import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeRequest
import app.igormatos.botaprarodar.domain.model.Withdraws
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.flow.flowOf
import java.util.*

val bike = Bike(name = "Caloi")

val bikeRequest = BikeRequest(
    id = "",
    isAvailable = true,
    inUse = false,
    name = "New Bicycle",
    orderNumber = 1010,
    serialNumber = "New Serial",
    createdDate = Date().toString()
)

val withdraw = Withdraws(id = "123", date = "12/03/2021", user = userFake)

val bikeWithWithdraws = Bike(name = "Caloi", withdraws = mutableListOf(withdraw))

val exception = Exception()

val listBikes = mutableListOf(bikeRequest)

val listBikes2 = mutableListOf(bike)

val flowSuccess = flowOf(SimpleResult.Success(listBikes))

val flowError = flowOf(SimpleResult.Error(exception))

val addDataResponseBike = AddDataResponse("New Bicycle")

val addDataResponseEditBike = AddDataResponse("Bicycle Edited")

val mapOfBikes = mapOf(
    Pair("123", BikeRequest()),
    Pair("456", BikeRequest()),
    Pair("789", BikeRequest()),
    Pair("098", BikeRequest()),
    Pair("876", BikeRequest())
)

val bikeSimpleSuccess = SimpleResult.Success(addDataResponseBike)

val bikeSimpleError = SimpleResult.Error(Exception())

val bikeSimpleSuccessEdit = SimpleResult.Success(addDataResponseEditBike)

fun generateBikeInUse(nameBicycle: String): BikeRequest {
    return BikeRequest().apply {
        name = nameBicycle
        orderNumber = System.currentTimeMillis()
        serialNumber = "123serial"
        photoPath = "http://bla.com"
        photoThumbnailPath = "http://bla.com"
        inUse = true
        communityId = "123"
    }
}

fun buildMapStringAndBicycleInUse(howMuch: Int): Map<String, BikeRequest> {
    val map = mutableMapOf<String, BikeRequest>()
    for (i in howMuch downTo 1) {
        map[i.toString()] = generateBikeInUse("bicycle $i")
    }
    return map
}

fun generateBike(nameBicycle: String): BikeRequest {
    return BikeRequest().apply {
        name = nameBicycle
        orderNumber = System.currentTimeMillis()
        serialNumber = "123serial"
        photoPath = "http://bla.com"
        photoThumbnailPath = "http://bla.com"
    }
}

fun buildMapStringAndBicycle(howMuch: Int): Map<String, BikeRequest> {
    val map = mutableMapOf<String, BikeRequest>()
    for (i in howMuch downTo 1) {
        map[i.toString()] = generateBike("bicycle $i")
    }
    return map
}