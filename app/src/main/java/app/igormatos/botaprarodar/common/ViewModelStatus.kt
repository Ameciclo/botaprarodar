package app.igormatos.botaprarodar.common

sealed class ViewModelStatus<out T> {
    data class Success<T>(val data: T) : ViewModelStatus<T>()
    object Loading : ViewModelStatus<Nothing>()
    data class Error<T>(val message: String) : ViewModelStatus<T>()
}