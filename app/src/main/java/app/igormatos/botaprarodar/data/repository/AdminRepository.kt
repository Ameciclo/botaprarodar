package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import java.lang.Exception

class AdminRepository(private val adminRemoteDataSource: AdminDataSource) {

    suspend fun createAdmin(
        email: String, password: String
    ): Admin {
        val firebaseUserUid = try {
            adminRemoteDataSource.createAdmin(email, password)?.id
                ?: throw UserAdminErrorException.AdminNotCreated
        } catch (e: FirebaseNetworkException) {
            throw UserAdminErrorException.AdminNetwork
        }

        return assembleAdmin(firebaseUserUid, email, password)
    }

    suspend fun authenticateAdmin(
        email: String, password: String
    ): Admin {
        val firebaseUserUid = try {
            adminRemoteDataSource.authenticateAdmin(email, password)?.id
                ?: throw UserAdminErrorException.AdminNotFound
        } catch (e: FirebaseNetworkException) {
            throw UserAdminErrorException.AdminNetwork
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw UserAdminErrorException.AdminNotFound
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
        } catch (e: Exception) {
            false
        }
    }
    private fun assembleAdmin(
        firebaseUid: String,
        email: String,
        password: String
    ): Admin =
        Admin(email = email, password = password, id = firebaseUid)
}
