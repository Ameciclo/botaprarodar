package app.igormatos.botaprarodar.common.util

import android.util.Patterns

fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()