package app.igormatos.botaprarodar.presentation.authentication.viewmodel

import app.igormatos.botaprarodar.common.enumType.BprError
import app.igormatos.botaprarodar.common.enumType.BprErrorType

sealed class SendPasswordRecoveryViewState {
    class SendError(override val type: BprErrorType): SendPasswordRecoveryViewState(),
        BprError
    object SendSuccess : SendPasswordRecoveryViewState()
    object SendLoading: SendPasswordRecoveryViewState()
}
