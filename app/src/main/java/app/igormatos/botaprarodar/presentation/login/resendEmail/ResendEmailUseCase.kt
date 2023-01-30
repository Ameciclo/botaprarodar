package app.igormatos.botaprarodar.presentation.login.resendEmail

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.login.signin.BprResult

class ResendEmailUseCase(private val adminRepository: AdminRepository) {
    suspend operator fun invoke() = try {
        if (adminRepository.sendEmailVerification())
            BprResult.Success(Unit)
        else BprResult.Failure(RuntimeException(), BprErrorType.UNKNOWN)
    } catch (e: UserAdminErrorException.AdminNetwork) {
        BprResult.Failure(e, BprErrorType.NETWORK)
    } catch (e: Exception) {
        BprResult.Failure(e, BprErrorType.UNKNOWN)
    }
}