package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import com.google.firebase.FirebaseNetworkException

class AdminRepository(private val firebaseAdminDataSource: FirebaseAdminDataSource) {

    suspend fun createAdmin(
        email: String, password: String
    ): Admin {
        val firebaseUserUid = try {
            firebaseAdminDataSource.createFirebaseUser(email, password)?.uid
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
            firebaseAdminDataSource.authenticateFirebaseUser(email, password)?.uid
                ?: throw UserAdminErrorException.AdminNotFound
        } catch (e: FirebaseNetworkException) {
            throw UserAdminErrorException.AdminNetwork
        }

        return assembleAdmin(firebaseUserUid, email, password)
    }

    private fun assembleAdmin(
        firebaseUid: String,
        email: String,
        password: String
    ): Admin =
        Admin(email = email, password = password, id = firebaseUid)

}
