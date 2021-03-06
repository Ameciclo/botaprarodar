package app.igormatos.botaprarodar.common.extensions

import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeRequest
import app.igormatos.botaprarodar.domain.model.Devolution
import app.igormatos.botaprarodar.domain.model.Withdraws
import java.text.SimpleDateFormat
import java.util.*

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

fun Bike.getLastWithdraw(): Withdraws? {
    val lastWithdraw = this.withdraws?.let { withdrawList ->
        withdrawList.mapNotNull { withdraw ->
            val dateFormater = SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss",
                Locale.getDefault()
            ).parse(withdraw.date)
            try {
                Pair(withdraw, dateFormater)
            } catch (e: Exception) {
                null
            }
        }.maxByOrNull {
            it.second
        }
    }

    return lastWithdraw?.first
}

fun Bike.getLastDevolution(): Devolution? {
    val lastDevolution = this.devolutions?.let { devolutionList ->
        devolutionList.mapNotNull { devolution ->
            val dateFormatter = SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss",
                Locale.getDefault()
            ).parse(devolution.date.orEmpty())
            try {
                Pair(devolution, dateFormatter)
            } catch (e: Exception) {
                null
            }
        }.maxByOrNull {
            it.second
        }
    }

    return lastDevolution?.first
}

fun List<Bike>.sortByOrderNumber(): List<Bike> {
    return this.sortedBy { bike -> bike.orderNumber }
}