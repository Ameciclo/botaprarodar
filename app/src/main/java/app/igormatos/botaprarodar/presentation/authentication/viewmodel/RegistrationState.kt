package app.igormatos.botaprarodar.presentation.authentication.viewmodel

import app.igormatos.botaprarodar.common.enumType.BprError
import app.igormatos.botaprarodar.common.enumType.BprErrorType

sealed class RegistrationState {
    object Completed: RegistrationState()
    object Error: RegistrationState()
    class SendError(override val type: BprErrorType): RegistrationState(),
        BprError
    object SendSuccess: RegistrationState()
}