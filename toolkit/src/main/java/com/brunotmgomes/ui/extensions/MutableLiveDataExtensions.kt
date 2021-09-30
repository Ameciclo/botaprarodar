package com.brunotmgomes.ui.extensions

import androidx.lifecycle.MutableLiveData

fun MutableLiveData<String>.isNotNullOrBlank() = !value.isNullOrBlank()

fun MutableLiveData<String>.isNullOrBlank() = value.isNullOrBlank()

fun <T> MutableLiveData<T>.isNotNull() = value != null