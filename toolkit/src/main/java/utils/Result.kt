package utils

sealed class Result<out T> {
    class Success<out T>(val data: T) : Result<T>()
    class Error(val exception: Exception) : Result<Nothing>()
}