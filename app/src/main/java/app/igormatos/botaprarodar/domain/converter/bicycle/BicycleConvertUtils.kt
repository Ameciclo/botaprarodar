package app.igormatos.botaprarodar.domain.converter.bicycle

import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.extensions.transformNullToEmpty

fun buildBicycleRequest(bike: Bike) =
    BicycleRequest(
        id = bike.id.transformNullToEmpty(),
        available = true,
        inUse = false,
        name = bike.name.transformNullToEmpty(),
        orderNumber = bike.orderNumber.transformNullToEmpty(),
        photoPath = bike.photoPath.transformNullToEmpty(),
        photoThumbnailPath = bike.photoThumbnailPath.transformNullToEmpty(),
        serialNumber = bike.serialNumber.transformNullToEmpty(),
        createdDate = bike.createdDate.transformNullToEmpty()
    )