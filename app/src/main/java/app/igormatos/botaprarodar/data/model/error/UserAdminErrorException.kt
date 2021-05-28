package app.igormatos.botaprarodar.data.model.error

import java.lang.Exception

sealed class UserAdminErrorException: Exception(){
    object AdminNetwork: UserAdminErrorException()
    object AdminNotCreated: UserAdminErrorException()
    object AdminPasswordInvalid: UserAdminErrorException()
    object AdminAccountNotFound: UserAdminErrorException()
    object AdminAccountAlreadyExists: UserAdminErrorException()
    object AdminEmailNotVerified: UserAdminErrorException()
}