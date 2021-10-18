package com.brunotmgomes.ui

sealed class SimpleResult<out T> {
    data class Success<out T>(val data: T) : SimpleResult<T>()
    data class Error(val exception: Exception) : SimpleResult<Nothing>()
}