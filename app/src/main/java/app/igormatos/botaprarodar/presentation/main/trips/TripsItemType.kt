package app.igormatos.botaprarodar.presentation.main.trips

import app.igormatos.botaprarodar.domain.model.BikeActivity

sealed class TripsItemType {
    data class TitleType(val title: String) : TripsItemType()
    data class BikeType(val bikeActivity: BikeActivity) : TripsItemType()
}