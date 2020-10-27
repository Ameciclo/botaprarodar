package app.igormatos.botaprarodar.screens

import app.igormatos.botaprarodar.data.model.Bicycle

interface BicycleAdapterListener {
    fun onBicycleClicked(bicycle: Bicycle)
    fun onBicycleLongClicked(bicycle: Bicycle) : Boolean
}