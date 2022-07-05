package app.igormatos.botaprarodar.presentation.login

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.authentication.Validator

class LoginUseCase(
    private val adminRepository: AdminRepository,
    private val emailValidator: Validator<String?>,
    private val passwordValidator: Validator<String?>,
) {

    suspend fun authenticateAdmin(email: String, password: String): LoginState {
        return try {
            val admin: Admin = adminRepository.authenticateAdmin(email, password)
            LoginState.Success(admin)
        } catch (e: UserAdminErrorException.AdminNetwork) {
            LoginState.Error(BprErrorType.NETWORK)
        } catch (e: UserAdminErrorException.AdminPasswordInvalid) {
            LoginState.Error(BprErrorType.INVALID_PASSWORD)
        } catch (e: UserAdminErrorException.AdminAccountNotFound) {
            LoginState.Error(BprErrorType.INVALID_ACCOUNT)
        } catch (e: UserAdminErrorException.AdminEmailNotVerified) {
            LoginState.Error(BprErrorType.EMAIL_NOT_VERIFIED)
        } catch (e: Exception) {
            LoginState.Error(BprErrorType.UNKNOWN)
        }
    }

    fun isEmailValid(email: String?) = emailValidator.validate(email?.trim())

    fun isPasswordValid(password: String?) = passwordValidator.validate(password)

    fun isLoginFormValid(email: String?, password: String?) =
        isEmailValid(email) && isPasswordValid(password)
}