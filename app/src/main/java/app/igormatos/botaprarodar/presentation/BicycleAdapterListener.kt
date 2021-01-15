package app.igormatos.botaprarodar.presentation

import app.igormatos.botaprarodar.domain.model.Bike

interface BicycleAdapterListener {
    fun onBicycleClicked(bike: Bike)
    fun onBicycleLongClicked(bike: Bike) : Boolean
}