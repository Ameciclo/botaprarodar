package app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike

sealed class BikeDevolutionUiState {
    object Success : BikeDevolutionUiState()
    object Loading : BikeDevolutionUiState()
    data class Error(val message: String) : BikeDevolutionUiState()
}

const val DEFAULT_WITHDRAW_ERROR_MESSAGE = "Falha ao realizar devolução, tente novamente"
