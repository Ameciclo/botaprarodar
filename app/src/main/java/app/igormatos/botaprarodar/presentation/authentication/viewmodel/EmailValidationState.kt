package app.igormatos.botaprarodar.presentation.authentication.viewmodel

import app.igormatos.botaprarodar.common.BprError
import app.igormatos.botaprarodar.common.BprErrorType

sealed class EmailValidationState {
    object InitialState: EmailValidationState()
    object Completed: EmailValidationState()
    class SendError(override val type: BprErrorType): EmailValidationState(), BprError
    class SendSuccess(val isAdminRegisted: Boolean): EmailValidationState()
    object SendLoading: EmailValidationState()
}
