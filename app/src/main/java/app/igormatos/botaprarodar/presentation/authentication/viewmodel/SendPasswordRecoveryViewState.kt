package app.igormatos.botaprarodar.presentation.authentication.viewmodel

import app.igormatos.botaprarodar.common.BprError
import app.igormatos.botaprarodar.common.BprErrorType

sealed class SendPasswordRecoveryViewState {
    class SendError(override val type: BprErrorType): SendPasswordRecoveryViewState(), BprError
    object SendSuccess : SendPasswordRecoveryViewState()
    object SendLoading: SendPasswordRecoveryViewState()
}
