package app.igormatos.botaprarodar.presentation.authentication.viewmodel

sealed class RegistrationState {
    object Error: RegistrationState()
    object Success: RegistrationState()
    object InvalidEmail: RegistrationState()
}