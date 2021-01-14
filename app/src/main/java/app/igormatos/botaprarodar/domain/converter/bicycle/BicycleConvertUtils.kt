package app.igormatos.botaprarodar.domain.converter.bicycle

import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.domain.model.Bike

fun buildBicycleRequest(bike: Bike) =
    BicycleRequest(
        name = bike.name!!,
        orderNumber = bike.order_number!!,
        photoPath = bike.photo_path!!,
        photoThumbnailPath = bike.photo_thumbnail_path!!,
        serialNumber = bike.serial_number!!,
        createdDate = bike.created_date!!)