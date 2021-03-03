package app.igormatos.botaprarodar.domain.usecase.trips

import app.igormatos.botaprarodar.common.enumType.BikeActionsMenuType

class BikeActionUseCase {

    fun getBikeActionsList(): List<BikeActionsMenuType>{
        return BikeActionsMenuType.values().toMutableList()
    }
}