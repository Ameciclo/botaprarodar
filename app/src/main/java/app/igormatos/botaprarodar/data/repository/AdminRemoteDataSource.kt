package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.model.Admin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AdminRemoteDataSource(private val firebaseAuth: FirebaseAuth) : AdminDataSource {
    suspend fun createAdmin(
        email: String,
        password: String
    ): FirebaseUser? = firebaseAuth
        .createUserWithEmailAndPassword(email, password)
        .await().user

    suspend fun authenticateAdmin(email: String, password: String): FirebaseUser? =
        firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .await().user

    suspend fun sendPasswordRecoverEmail(email: String) {
        firebaseAuth
            .sendPasswordResetEmail(email)
            .await()
    }

    suspend fun isAdminRegistered(email: String) = firebaseAuth
        .fetchSignInMethodsForEmail(email)
        .await().signInMethods.isNullOrEmpty().not()

    private fun assembleAdmin(
        firebaseUid: String,
        email: String,
        password: String
    ): Admin =
        Admin(email = email, password = password, id = firebaseUid)
}
