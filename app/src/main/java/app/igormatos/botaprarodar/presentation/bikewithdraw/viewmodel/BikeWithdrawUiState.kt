package app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel

sealed class BikeWithdrawUiState {
    object Success : BikeWithdrawUiState()
    object Loading : BikeWithdrawUiState()
    data class Error(val message: String) : BikeWithdrawUiState()
}

const val DEFAULT_WITHDRAW_ERROR_MESSAGE = "Falha ao realizar empréstimo, tente novamente"
