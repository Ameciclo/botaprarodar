package app.igormatos.botaprarodar.utils

import app.igormatos.botaprarodar.domain.model.*
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.flow.flowOf
import java.util.*

val bike = Bike(name = "Caloi")

val bike2 = Bike(
    id = "",
    isAvailable = true,
    inUse = false,
    name = "New Bicycle",
    orderNumber = 1010,
    serialNumber = "New Serial",
    createdDate = Date().toString()
)

val bike3 = Bike(name = "Monark")

val bikeRequest = BikeRequest(
    id = "",
    isAvailable = true,
    inUse = false,
    name = "New Bicycle",
    orderNumber = 1010,
    serialNumber = "New Serial",
    createdDate = Date().toString()
)


val mapOfWithdraws = mapOf(
    Pair("123", Withdraws(id = "111")),
    Pair("456", Withdraws(id = "222")),
    Pair("789", Withdraws(id = "333"))
)

val mapOfDevolutions = mapOf(
    Pair("123", Devolution()),
    Pair("456", Devolution()),
    Pair("789", Devolution())
)

val bikeRequestWithMappers = BikeRequest(
    id = "1",
    isAvailable = true,
    inUse = false,
    name = "New Bicycle",
    orderNumber = 1010,
    serialNumber = "New Serial",
    createdDate = Date().toString(),
    withdraws = mapOfWithdraws,
    devolutions = mapOfDevolutions
)

val withdraw = Withdraws(id = "123", date = "12/03/2021 10:00:00", user = userFake)

val devolution = Devolution(id = "098", date = "15/03/2021", user = userFake)

val bikeWithWithdraws = Bike(
    name = "Caloi",
    withdraws = mutableListOf(withdraw),
    devolutions = mutableListOf(devolution)
)

val mapOfBikesRequest = mapOf(
    Pair("123", BikeRequest(withdraws = mapOfWithdraws, devolutions = mapOfDevolutions)),
    Pair("456", BikeRequest()),
    Pair("789", BikeRequest()),
    Pair("098", BikeRequest()),
    Pair("876", BikeRequest())
)

val exception = Exception()

val listBikeRequest = mutableListOf(bikeRequest)

val listBikeRequestWithdrawsDevolutions = mutableListOf(bikeRequestWithMappers)

val listBikes = mutableListOf(bike2)

val flowSuccess = flowOf(SimpleResult.Success(listBikeRequest))

val flowError = flowOf(SimpleResult.Error(exception))

val addDataResponseBike = AddDataResponse("New Bicycle")

val addDataResponseEditBike = AddDataResponse("Bicycle Edited")

val bikeSimpleSuccess = SimpleResult.Success(addDataResponseBike)

val bikeSimpleError = SimpleResult.Error(Exception())

val bikeSimpleSuccessEdit = SimpleResult.Success(addDataResponseEditBike)

fun generateBikeHolder(): BikeHolder {
    return BikeHolder().apply {
        bike = bikeWithWithdraws
    }
}

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

fun generateBikeInNotUse(nameBicycle: String): BikeRequest {
    return BikeRequest().apply {
        name = nameBicycle
        orderNumber = System.currentTimeMillis()
        serialNumber = "123serial"
        photoPath = "http://bla.com"
        photoThumbnailPath = "http://bla.com"
        inUse = false
        communityId = communityFixture.id
    }
}

fun buildMapStringAndBicycleInUse(howMuch: Int): Map<String, BikeRequest> {
    val map = mutableMapOf<String, BikeRequest>()
    for (i in howMuch downTo 1) {
        map[i.toString()] = generateBikeInUse("bicycle $i")
    }
    return map
}

fun buildMapStringAndBicycleInNotUse(howMuch: Int): Map<String, BikeRequest> {
    val map = mutableMapOf<String, BikeRequest>()
    for (i in howMuch downTo 1) {
        map[i.toString()] = generateBikeInNotUse("bicycle $i")
    }
    return map
}

fun buildMapStringAndBicycleRandom(
    howMuchBikesInUse: Int,
    howMuchBikesInNotUse: Int
): Map<String, BikeRequest> {
    val map = mutableMapOf<String, BikeRequest>()
    for (i in howMuchBikesInUse downTo 1) {
        map[i.toString()] = generateBikeInUse("bicycle $i")
    }
    for (i in howMuchBikesInNotUse downTo 1) {
        map[i.toString()] = generateBikeInNotUse("bicycle $i")
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

val communityFixture = Community().apply {
    id = "some id"
}

val availableBikes = listOf(
    Bike().apply {
        name = "caloi"
        inUse = false
        communityId = communityFixture.id
    },
    Bike().apply {
        name = "caloi"
        inUse = false
        communityId = "123"
    }
)

val borrowedBikes = listOf(
    Bike().apply {
        name = "monark"
        inUse = true
        communityId = "123"
    },
    Bike().apply {
        name = "monark"
        inUse = true
        communityId = "123"
    },
)

val bikeList = borrowedBikes.plus(availableBikes)