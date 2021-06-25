package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.network.api.AdminApiService
import app.igormatos.botaprarodar.domain.model.admin.AdminMapper
import app.igormatos.botaprarodar.domain.model.admin.AdminRequest
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import retrofit2.Response
import java.net.UnknownHostException

class AdminRepository(
    private val adminRemoteDataSource: AdminDataSource,
    private val adminApiService: AdminApiService,
    private val adminMapper: AdminMapper
) {

    suspend fun createAdmin(
        email: String, password: String
    ): Admin {
        val firebaseUserUid = try {
            adminRemoteDataSource.createAdmin(email, password)?.id
                ?: throw UserAdminErrorException.AdminNotCreated
        } catch (e: FirebaseNetworkException) {
            throw UserAdminErrorException.AdminNetwork
        } catch (e: FirebaseAuthUserCollisionException) {
            throw UserAdminErrorException.AdminAccountAlreadyExists
        } catch (e: Exception) {
            throw UserAdminErrorException.AdminNotCreated
        }
        return assembleAdmin(firebaseUserUid, email, password)
    }

    suspend fun authenticateAdmin(
        email: String, password: String
    ): Admin {
        val firebaseUserUid = try {
            adminRemoteDataSource.authenticateAdmin(email, password)?.id
                ?: throw UserAdminErrorException.AdminAccountNotFound
        } catch (e: FirebaseNetworkException) {
            throw UserAdminErrorException.AdminNetwork
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw UserAdminErrorException.AdminPasswordInvalid
        } catch (e: FirebaseAuthInvalidUserException) {
            throw UserAdminErrorException.AdminAccountNotFound
        }

        return assembleAdmin(firebaseUserUid, email, password)
    }

    suspend fun isAdminRegistered(
        email: String
    ): Boolean {
        return try {
            adminRemoteDataSource.isAdminRegistered(email)
        } catch (e: FirebaseNetworkException) {
            throw UserAdminErrorException.AdminNetwork
        } catch (e: Exception) {
            false
        }
    }

    suspend fun sendPasswordResetEmail(
        email: String
    ): Boolean {
        return try {
            adminRemoteDataSource.sendPasswordRecoverEmail(email)
            true
        } catch (e: FirebaseNetworkException) {
            throw UserAdminErrorException.AdminNetwork
        } catch (e: FirebaseAuthInvalidUserException) {
            throw UserAdminErrorException.AdminAccountNotFound
        } catch (e: Exception) {
            false
        }
    }

    suspend fun sendEmailVerification(): Boolean {
        return try {
            adminRemoteDataSource.sendEmailVerification()
            true
        } catch (e: FirebaseNetworkException) {
            throw UserAdminErrorException.AdminNetwork
        } catch (e: FirebaseAuthInvalidUserException) {
            throw UserAdminErrorException.AdminAccountNotFound
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getAdminById(id: String): Admin? {
        return try {
            val response: Response<AdminRequest> = adminApiService.getAdminById(id)
            if (response.isSuccessful && response.body() != null) {
                return adminMapper.adminRequestToAdmin(response.body()!!)
            }
            return null
        } catch (e: UnknownHostException) {
            throw UserAdminErrorException.AdminNetwork
        }
    }

    private fun assembleAdmin(
        firebaseUid: String,
        email: String,
        password: String
    ): Admin =
        Admin(email = email, password = password, id = firebaseUid)
}
