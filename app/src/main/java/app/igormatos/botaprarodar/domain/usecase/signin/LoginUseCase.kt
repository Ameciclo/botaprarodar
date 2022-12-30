package app.igormatos.botaprarodar.domain.usecase.signin

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.login.signin.SignInResult

class LoginUseCase(private val adminRepository: AdminRepository) {
    suspend operator fun invoke(email: String, password: String) = try {
        val admin = adminRepository.authenticateAdmin(email, password)
        SignInResult.Success(admin)
    } catch (e: UserAdminErrorException.AdminNetwork) {
        SignInResult.Failure(e, BprErrorType.NETWORK)
    } catch (e: UserAdminErrorException.AdminPasswordInvalid) {
        SignInResult.Failure(e, BprErrorType.INVALID_PASSWORD)
    } catch (e: UserAdminErrorException.AdminAccountNotFound) {
        SignInResult.Failure(e, BprErrorType.INVALID_ACCOUNT)
    } catch (e: UserAdminErrorException.AdminEmailNotVerified) {
        SignInResult.Failure(e, BprErrorType.EMAIL_NOT_VERIFIED)
    } catch (e: Exception) {
        SignInResult.Failure(e, BprErrorType.UNKNOWN)
    }
}