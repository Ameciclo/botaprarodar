package app.igormatos.botaprarodar.common.extensions

import app.igormatos.botaprarodar.presentation.main.trips.TripsItemType
import java.text.SimpleDateFormat
import java.util.*

fun List<TripsItemType>.orderByDate(): MutableList<TripsItemType> {
    return this.let { bikeTypeList ->
        bikeTypeList.mapNotNull { bikeType ->
            val dateFormatter = SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).parse((bikeType as TripsItemType.BikeType).bikeActivity.date.toString())

            try {
                Pair(bikeType, dateFormatter)
            } catch (e: Exception) {
                null
            }
        }.sortedBy {
            it.second
        }.map {
            it.first
        } as MutableList<TripsItemType>
    }
}