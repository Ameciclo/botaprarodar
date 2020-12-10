package app.igormatos.botaprarodar.domain.converter.bicycle

import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.domain.model.Bicycle

fun buildBicycleRequest(bicycle: Bicycle) =
    BicycleRequest(
        name = bicycle.name!!,
        orderNumber = bicycle.order_number!!,
        photoPath = bicycle.photo_path!!,
        photoThumbnailPath = bicycle.photo_thumbnail_path!!,
        serialNumber = bicycle.serial_number!!,
        createdDate = bicycle.created_date!!)