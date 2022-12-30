package app.igormatos.botaprarodar.presentation.login.resendEmail

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.login.signin.SignInResult

class ResendEmailUseCase(private val adminRepository: AdminRepository) {
    suspend operator fun invoke() = try {
        if (adminRepository.sendEmailVerification())
            SignInResult.Success(Unit)
        else SignInResult.Failure(RuntimeException(), BprErrorType.UNKNOWN)
    } catch (e: UserAdminErrorException.AdminNetwork) {
        SignInResult.Failure(e, BprErrorType.NETWORK)
    } catch (e: Exception) {
        SignInResult.Failure(e, BprErrorType.UNKNOWN)
    }
}