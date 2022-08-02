package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.authentication.Validator

class PasswordRecoveryUseCase(
    private val adminRepository: AdminRepository,
    private val emailValidator: Validator<String?>
) {

    suspend fun sendPasswordResetEmail(email: String): PasswordRecoveryState {
        return try {
            if (adminRepository.sendPasswordResetEmail(email))
                PasswordRecoveryState.Success
            else PasswordRecoveryState.Error(BprErrorType.UNKNOWN)
        } catch (e: UserAdminErrorException.AdminNetwork) {
            PasswordRecoveryState.Error(BprErrorType.NETWORK)
        } catch (e: UserAdminErrorException.AdminAccountNotFound) {
            PasswordRecoveryState.Error(BprErrorType.INVALID_ACCOUNT)
        } catch (e: Exception) {
            PasswordRecoveryState.Error(BprErrorType.UNKNOWN)
        }
    }

    fun isEmailValid(email: String?) = emailValidator.validate(email)
}