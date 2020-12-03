package app.igormatos.botaprarodar.presentation

import app.igormatos.botaprarodar.domain.model.Bicycle

interface BicycleAdapterListener {
    fun onBicycleClicked(bicycle: Bicycle)
    fun onBicycleLongClicked(bicycle: Bicycle) : Boolean
}