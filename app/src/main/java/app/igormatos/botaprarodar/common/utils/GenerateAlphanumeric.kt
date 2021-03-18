package app.igormatos.botaprarodar.common.utils

private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun generateRandomAlphanumeric(): String {
    return (1..20)
        .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}