package app.igormatos.botaprarodar.common

interface BprError {
    val type: BprErrorType
}

enum class BprErrorType {
    NETWORK,
    UNKNOWN
}
