package app.igormatos.botaprarodar.presentation.authentication.viewmodel

import app.igormatos.botaprarodar.common.enumType.BprError
import app.igormatos.botaprarodar.common.enumType.BprErrorType

sealed class EmailValidationState {
    object InitialState: EmailValidationState()
    object Completed: EmailValidationState()
    class SendError(override val type: BprErrorType): EmailValidationState(),
        BprError
    class SendSuccess(val isAdminRegisted: Boolean): EmailValidationState()
    object SendLoading: EmailValidationState()
}
