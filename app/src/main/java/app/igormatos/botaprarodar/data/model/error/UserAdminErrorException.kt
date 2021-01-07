package app.igormatos.botaprarodar.data.model.error

import java.lang.Exception

sealed class UserAdminErrorException: Exception(){
    object AdminNetwork: UserAdminErrorException()
    object AdminNotCreated: UserAdminErrorException()
    object AdminNotFound: UserAdminErrorException()
}