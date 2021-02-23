package app.igormatos.botaprarodar.common.enumType

interface BprError {
    val type: BprErrorType
}

enum class BprErrorType {
    NETWORK,
    UNKNOWN,
    UNAUTHORIZED
}
