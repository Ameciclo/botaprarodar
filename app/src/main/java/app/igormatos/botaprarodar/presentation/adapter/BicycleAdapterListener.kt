package app.igormatos.botaprarodar.presentation.adapter

import app.igormatos.botaprarodar.domain.model.Bike

interface BicycleAdapterListener {
    fun navigateToBikeFormFragment(bike: Bike?)
    fun onBicycleLongClicked(bike: Bike) : Boolean
}