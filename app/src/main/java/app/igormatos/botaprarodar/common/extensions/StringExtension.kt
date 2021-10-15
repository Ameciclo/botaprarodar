package app.igormatos.botaprarodar.common.extensions

fun String? .orValue(value: String): String {
    if(this.isNullOrEmpty()) {
        return value
    }
    return this
}
