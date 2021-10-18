package app.igormatos.botaprarodar.common.extensions

import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeRequest

fun <T, E> Map<in T, E>.convertToList(): MutableList<E> {
    return this.values.toMutableList()
}

fun Map<String, List<String>>.getIndexFromList( keyMap: String, listValue: String ): Int {
   return this[keyMap]?.indexOfLast{ listValue == it }.takeIf { it != null &&  it > -1 } ?: 0
}

fun Map<String, BikeRequest>.convertMapperToBikeList(): MutableList<Bike> {
    val list = this.convertToList()
    val listToReturn = mutableListOf<Bike>()

    list.forEachIndexed { index, bikeRequest ->
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
        bikeRequest.withdraws?.let { withdraws ->
            val listWithdraws = withdraws.convertToList()
            bike.withdraws = listWithdraws
        }
        bikeRequest.devolutions?.let { devolutions ->
            val listDevolutions = devolutions.convertToList()
            bike.devolutions = listDevolutions
        }
        listToReturn.add(bike)
    }
    return listToReturn
}
