package app.igormatos.botaprarodar.presentation.login.resendEmail

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository

class ResendEmailUseCase(
    private val adminRepository: AdminRepository
) {
    suspend fun execute(): ResendEmailState {
        return try {
            if (adminRepository.sendEmailVerification())
                ResendEmailState.Success
            else ResendEmailState.Error(BprErrorType.UNKNOWN)
        } catch (e: UserAdminErrorException.AdminNetwork) {
            ResendEmailState.Error(BprErrorType.NETWORK)
        } catch (e: Exception) {
            ResendEmailState.Error(BprErrorType.UNKNOWN)
        }
    }
}