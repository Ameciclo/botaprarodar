package app.igormatos.botaprarodar.common.extensions

import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeRequest

fun <T, E> Map<in T, E>.convertToList(): MutableList<E> {
    return this.values.toMutableList()
}

fun Map<String, BikeRequest>.convertMapperToBikeList(): MutableList<Bike> {
    val list = this.convertToList()
    val listToReturn = mutableListOf<Bike>()

    list.forEachIndexed { index, bikeRequest ->
        bikeRequest.withdraws?.let { withdraws ->
            val bike = Bike().apply {
                name = bikeRequest.name
                communityId = bikeRequest.communityId
                serialNumber = bikeRequest.serialNumber
                orderNumber = bikeRequest.orderNumber
                createdDate = bikeRequest.createdDate
                inUse = bikeRequest.inUse
                photoPath = bikeRequest.photoPath
                photoThumbnailPath = bikeRequest.photoThumbnailPath
                id = bikeRequest.id
                isAvailable = bikeRequest.isAvailable
            }
            val listWithdraws = withdraws.convertToList()
            bike.withdraws = listWithdraws
            listToReturn.add(bike)
        }
    }
    return listToReturn
}
