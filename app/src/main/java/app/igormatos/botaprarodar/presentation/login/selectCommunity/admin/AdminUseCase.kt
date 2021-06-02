package app.igormatos.botaprarodar.presentation.login.selectCommunity.admin

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository

class AdminUseCase(
    private val adminRepository: AdminRepository
) {
    suspend fun isAdmin(idUser: String): AdminState {
        return try {
            val admin: Admin? = adminRepository.getAdminById(idUser)
            if (admin != null) AdminState.IsAdmin
            else AdminState.NotIsAdmin
        } catch (exception: UserAdminErrorException.AdminNetwork) {
            AdminState.Error(BprErrorType.NETWORK)
        } catch (exception: Exception) {
            AdminState.Error(BprErrorType.UNKNOWN)
        }
    }
}