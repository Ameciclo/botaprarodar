package app.igormatos.botaprarodar.common.extensions

import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeRequest

fun Bike.convertToBikeRequest(): BikeRequest {
    val withdrawsMap = this.withdraws?.map {
        it.id to it
    }?.toMap()

    val devolutionsMap = this.devolutions?.map {
        it.id to it
    }?.toMap()

    return BikeRequest().apply {
        id = this@convertToBikeRequest.id
        orderNumber = this@convertToBikeRequest.orderNumber
        serialNumber = this@convertToBikeRequest.serialNumber
        communityId = this@convertToBikeRequest.communityId
        name = this@convertToBikeRequest.name
        photoThumbnailPath = this@convertToBikeRequest.photoThumbnailPath
        photoPath = this@convertToBikeRequest.photoPath
        inUse = this@convertToBikeRequest.inUse
        createdDate = this@convertToBikeRequest.createdDate
        path = this@convertToBikeRequest.path
        isAvailable = this@convertToBikeRequest.isAvailable
        withdraws = withdrawsMap
        devolutions = devolutionsMap
    }
}