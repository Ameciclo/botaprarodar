package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.model.Admin
import java.lang.IllegalStateException

class AdminRepository(private val firebaseAdminDataSource: FirebaseAdminDataSource) {

    suspend fun saveAdmin(
        email: String, password: String
    ): Admin? {
        return try {
            val firebaseUser = firebaseAdminDataSource.createFirebaseUser(email, password)

            val firebaseUid = firebaseAdminDataSource.getFirebaseUserUid(firebaseUser)

            return createAdmin(firebaseUid, email, password)

        } catch (e: Exception) {
            null
        }
    }

    private fun createAdmin(
        firebaseUid: String?,
        email: String,
        password: String
    ): Admin {
        firebaseUid?.let {
            return Admin(email = email, password = password, id = it)
        }

        throw IllegalStateException()
    }
}
