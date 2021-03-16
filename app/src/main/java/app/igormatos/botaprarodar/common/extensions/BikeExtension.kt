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

    return BikeRequest(
        id = this.id,
        orderNumber = this.orderNumber,
        serialNumber = this.serialNumber,
        communityId = this.communityId,
        name = this.name,
        photoThumbnailPath = this.photoThumbnailPath,
        photoPath = this.photoPath,
        inUse = this.inUse,
        createdDate = this.createdDate,
        path = this.path,
        isAvailable = this.isAvailable,
        withdraws = withdrawsMap,
        devolutions = devolutionsMap
    )
}