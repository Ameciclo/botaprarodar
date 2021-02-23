package app.igormatos.botaprarodar.presentation.authentication.viewmodel

import app.igormatos.botaprarodar.common.enumType.BprError
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.Admin

sealed class SignInViewState {
    class SendError(override val type: BprErrorType): SignInViewState(),
        BprError
    class SendSuccess(val admin: Admin): SignInViewState()
    object SendLoading: SignInViewState()
}
