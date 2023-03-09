package app.igormatos.botaprarodar.presentation.login.registration

sealed class RegisterEvent {
    object RegisterSuccessful : RegisterEvent()
    class Loading(val show: Boolean): RegisterEvent()
    object Finish : RegisterEvent()
}