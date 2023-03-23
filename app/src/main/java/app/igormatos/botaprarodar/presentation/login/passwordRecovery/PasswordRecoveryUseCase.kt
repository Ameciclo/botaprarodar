package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.login.signin.BprResult

class PasswordRecoveryUseCase(private val adminRepository: AdminRepository) {

    suspend operator fun invoke(email: String) = try {
        if (adminRepository.sendPasswordResetEmail(email))
            BprResult.Success(Unit)
        else BprResult.Failure(error = BprErrorType.UNKNOWN)
    } catch (e: UserAdminErrorException.AdminNetwork) {
        BprResult.Failure(e, BprErrorType.NETWORK)
    } catch (e: UserAdminErrorException.AdminAccountNotFound) {
        BprResult.Failure(e, BprErrorType.INVALID_ACCOUNT)
    } catch (e: Exception) {
        BprResult.Failure(e, BprErrorType.UNKNOWN)
    }
}