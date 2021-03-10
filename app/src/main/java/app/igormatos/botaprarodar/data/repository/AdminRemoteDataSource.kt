package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.model.Admin
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AdminRemoteDataSource(private val firebaseAuth: FirebaseAuth) : AdminDataSource {
    override suspend fun createAdmin(
        email: String,
        password: String
    ): Admin? {
        return try {
            val result = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await().user
            assembleAdmin(result.uid, email, password)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun authenticateAdmin(email: String, password: String): Admin? {
        val result = firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .await().user
        return assembleAdmin(result.uid, email, password)
    }

    override suspend fun sendPasswordRecoverEmail(email: String) {
        firebaseAuth
            .sendPasswordResetEmail(email)
            .await()
    }

    override suspend fun isAdminRegistered(email: String): Boolean = firebaseAuth
        .fetchSignInMethodsForEmail(email)
        .await().signInMethods.isNullOrEmpty().not()

    private fun assembleAdmin(
        firebaseUid: String,
        email: String,
        password: String
    ): Admin =
        Admin(email = email, password = password, id = firebaseUid)
}
