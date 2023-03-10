package app.igormatos.botaprarodar.domain.usecase.signin

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.login.signin.BprResult

class LoginUseCase(private val adminRepository: AdminRepository) {
    suspend operator fun invoke(email: String, password: String) = try {
        val admin = adminRepository.authenticateAdmin(email, password)
        BprResult.Success(admin)
    } catch (e: UserAdminErrorException.AdminNetwork) {
        BprResult.Failure(e, BprErrorType.NETWORK)
    } catch (e: UserAdminErrorException.AdminPasswordInvalid) {
        BprResult.Failure(e, BprErrorType.INVALID_PASSWORD)
    } catch (e: UserAdminErrorException.AdminAccountNotFound) {
        BprResult.Failure(e, BprErrorType.INVALID_ACCOUNT)
    } catch (e: UserAdminErrorException.AdminEmailNotVerified) {
        BprResult.Failure(e, BprErrorType.EMAIL_NOT_VERIFIED)
    } catch (e: Exception) {
        BprResult.Failure(e, BprErrorType.UNKNOWN)
    }
}