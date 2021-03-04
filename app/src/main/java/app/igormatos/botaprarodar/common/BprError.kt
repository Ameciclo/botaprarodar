package app.igormatos.botaprarodar.common

import app.igormatos.botaprarodar.common.enumType.BprErrorType


interface BprError {
    val type: BprErrorType
}