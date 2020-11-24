package utils

sealed class SimpleResult<out T> {
    class Success<out T>(val data: T) : SimpleResult<T>()
    class Error(val exception: Exception) : SimpleResult<Nothing>()
}