package com.brunotmgomes.ui.extensions

import android.util.Patterns

fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
fun CharSequence?.isValidPassword() = !isNullOrEmpty() && this.length >= 6