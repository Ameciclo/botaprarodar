package app.igormatos.botaprarodar.screens

import app.igormatos.botaprarodar.data.model.Bicycle
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
interface BicycleAdapterListener {
    fun onBicycleClicked(bicycle: Bicycle)
    fun onBicycleLongClicked(bicycle: Bicycle) : Boolean
}