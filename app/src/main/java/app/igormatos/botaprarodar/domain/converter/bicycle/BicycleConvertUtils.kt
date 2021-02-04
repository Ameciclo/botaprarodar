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
        orderNumber = bike.order_number.transformNullToEmpty(),
        photoPath = bike.photo_path.transformNullToEmpty(),
        photoThumbnailPath = bike.photo_thumbnail_path.transformNullToEmpty(),
        serialNumber = bike.serial_number.transformNullToEmpty(),
        createdDate = bike.created_date.transformNullToEmpty()
    )