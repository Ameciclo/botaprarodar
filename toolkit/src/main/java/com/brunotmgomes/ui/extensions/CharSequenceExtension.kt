package com.brunotmgomes.ui.extensions

import android.util.Patterns

fun CharSequence?.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun CharSequence?.isValidPassword() = !isNullOrEmpty() && this.length >= 6

fun String?.transformNullToEmpty(): String {
    return this?.let {
        this
    } ?: ""
}

fun String.getDateFromDateTime() = this.substring(0, 10)

fun String?.isNotNullOrNotEmpty() = !this.isNullOrBlank()

fun String?.isValidTelephone() = !this.isNullOrBlank() && (Patterns.PHONE.matcher(this).matches() || this.length == 13 || this.length == 12)
