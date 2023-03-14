package app.igormatos.botaprarodar.common.extensions

import android.util.Patterns

fun String?.orValue(value: String): String {
    if(this.isNullOrEmpty()) {
        return value
    }
    return this
}

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword() = this.isNotEmpty() && this.length >= 6
