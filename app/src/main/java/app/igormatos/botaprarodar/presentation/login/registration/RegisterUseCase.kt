package app.igormatos.botaprarodar.presentation.login.registration

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.authentication.Validator

class RegisterUseCase(
    private val adminRepository: AdminRepository,
    private val emailValidator: Validator<String?>,
    private val passwordValidator: Validator<String?>
) {

    suspend fun register(email: String, password: String): RegisterState {
        return try {
            adminRepository.createAdmin(email, password)
            RegisterState.Success
        } catch (e: UserAdminErrorException.AdminNetwork) {
            RegisterState.Error(type = BprErrorType.NETWORK)
        } catch (e: UserAdminErrorException.AdminAccountAlreadyExists) {
            RegisterState.Error(type = BprErrorType.INVALID_ACCOUNT)
        } catch (e: Exception) {
            RegisterState.Error(type = BprErrorType.UNKNOWN)
        }
    }

    fun isRegisterFormValid(email: String?, password: String?, confirmPassword: String?): Boolean {
        return emailValidator.validate(email)
                && passwordValidator.validate(password)
                && password == confirmPassword
    }
}