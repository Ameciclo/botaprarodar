package app.igormatos.botaprarodar.presentation.login.registration

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.login.signin.BprResult

class RegisterUseCase(private val adminRepository: AdminRepository) {

    suspend operator fun invoke(email: String, password: String): BprResult<Unit> {
        return try {
            adminRepository.createAdmin(email, password)
            BprResult.Success(Unit)
        } catch (e: UserAdminErrorException.AdminNetwork) {
            BprResult.Failure(e, BprErrorType.NETWORK)
        } catch (e: UserAdminErrorException.AdminAccountAlreadyExists) {
            BprResult.Failure(e, BprErrorType.INVALID_ACCOUNT)
        } catch (e: Exception) {
            BprResult.Failure(e, BprErrorType.UNKNOWN)
        }
    }
}