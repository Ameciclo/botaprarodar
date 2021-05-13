package app.igormatos.botaprarodar.common.extensions

import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeRequest

fun List<BikeRequest>.convertToBikeList(): MutableList<Bike> {
    val listToReturn = mutableListOf<Bike>()

    this.forEach { bikeRequest ->
        listToReturn.add(bikeRequest.convertToBike())
    }

    return listToReturn
}

fun BikeRequest.convertToBike(): Bike {
    val bike = Bike().apply {
        name = this@convertToBike.name
        communityId = this@convertToBike.communityId
        serialNumber = this@convertToBike.serialNumber
        orderNumber = this@convertToBike.orderNumber
        createdDate = this@convertToBike.createdDate
        inUse = this@convertToBike.inUse
        photoPath = this@convertToBike.photoPath
        photoThumbnailPath = this@convertToBike.photoThumbnailPath
        id = this@convertToBike.id
        isAvailable = this@convertToBike.isAvailable
    }

    this.withdraws?.let { withdraws ->
        val listWithdraws = withdraws.convertToList()
        bike.withdraws = listWithdraws
    }

    this.devolutions?.let { devolutions ->
        val listDevolutions = devolutions.convertToList()
        bike.devolutions = listDevolutions
    }

    return bike
}