package app.igormatos.botaprarodar.common.utils

import java.text.SimpleDateFormat
import java.util.*

fun formatAsJSONValidType(value: String): String {
    return "\"$value\""
}

fun formattedDate(format: String = "dd/MM/yyyy HH:mm:ss") =
    SimpleDateFormat(format, Locale("pt", "BR"))

fun String.formattedDate() = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale("pt", "BR"))
    .parse(this)?.let {
        SimpleDateFormat("dd MMM yyyy", Locale("pt", "BR"))
            .format(it)
            .replace(" ", " de ")
    }.toString()