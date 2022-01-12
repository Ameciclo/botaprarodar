package app.igormatos.botaprarodar.presentation.main

import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.components.CyclistActions

data class HomeUiState(
    var totalBikes: Int = 0,
    var totalBikesAvailable: Int = 0,
    var totalBikesWithdraw: Int = 0,
    var users: List<User> = listOf(),
    val cyclistActions: CyclistActions = CyclistActions()
) {
    companion object {
        fun fromBikes(bikes: List<Bike>): HomeUiState {
            val totalBikes = bikes.count()
            val totalBikesWithdraw = bikes.filter { it.inUse }.count()
            val totalBikesAvailable = totalBikes.minus(totalBikesWithdraw)

            return HomeUiState(totalBikes, totalBikesAvailable, totalBikesWithdraw)
        }
    }
}