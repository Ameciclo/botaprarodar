package app.igormatos.botaprarodar.common

sealed class BikeFormStatus {
    data class Success(val data: String) : BikeFormStatus()
    object Loading : BikeFormStatus()
    data class Error(val message: String) : BikeFormStatus()
}
