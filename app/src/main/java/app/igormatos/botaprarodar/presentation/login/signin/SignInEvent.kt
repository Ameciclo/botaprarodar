package app.igormatos.botaprarodar.presentation.login.signin

import app.igormatos.botaprarodar.data.model.Admin

sealed class SignInEvent {
    class SignInSuccessful(val admin: Admin): SignInEvent()
    class Navigate(val route: SignInRoute): SignInEvent()
    class Loading(val show: Boolean): SignInEvent()
    object EmailWarning : SignInEvent()
}


enum class SignInRoute {
    FORGOT_PASSWORD,
    REGISTER_USER
}