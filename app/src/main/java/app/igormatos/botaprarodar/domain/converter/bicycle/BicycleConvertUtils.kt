package app.igormatos.botaprarodar.domain.converter.bicycle

import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.domain.model.Bike

fun buildBicycleRequest(bike: Bike) =
    BicycleRequest(
        name = bike.name!!,
        orderNumber = bike.orderNumber!!,
        photoPath = bike.photoPath!!,
        photoThumbnailPath = bike.photoThumbnailPath!!,
        serialNumber = bike.serialNumber!!,
        createdDate = bike.createdDate!!)