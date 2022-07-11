package app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike

sealed class UiState {
    object Success : UiState()
    object Loading : UiState()
    data class Error(val message: String) : UiState()
}

const val DEFAULT_WITHDRAW_ERROR_MESSAGE = "Falha ao realizar devolução, tente novamente"
const val DEFAULT_RETURNS_ERROR_MESSAGE = "Falha ao realizar devolução, tente novamente"
