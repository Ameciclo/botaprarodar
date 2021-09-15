package com.brunotmgomes.ui.extensions

import androidx.core.util.PatternsCompat

fun CharSequence?.isValidEmail() =
    !isNullOrEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()

fun CharSequence?.isValidPassword() = !isNullOrEmpty() && this.length >= 6

fun String?.transformNullToEmpty(): String {
    return this?.let {
        this
    } ?: ""
}

fun String?.isNotNullOrNotBlank() = !this.isNullOrBlank()

fun String?.isValidTelephone() = !this.isNullOrEmpty() && (this.length == 13 || this.length == 12)
