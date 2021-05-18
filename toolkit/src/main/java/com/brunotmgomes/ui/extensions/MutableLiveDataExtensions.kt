package com.brunotmgomes.ui.extensions

import androidx.lifecycle.MutableLiveData

fun MutableLiveData<String>.isNotNullOrBlank() = !value.isNullOrBlank()

fun <T> MutableLiveData<T>.isNotNull() = value != null