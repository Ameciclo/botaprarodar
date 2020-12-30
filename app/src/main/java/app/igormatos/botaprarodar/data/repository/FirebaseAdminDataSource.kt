package app.igormatos.botaprarodar.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAdminDataSource(private val firebaseAuth: FirebaseAuth) {
    suspend fun createFirebaseUser(
        email: String,
        password: String
    ): FirebaseUser? = firebaseAuth
        .createUserWithEmailAndPassword(email, password)
        .await().user

    fun getFirebaseUserUid(firebaseUser: FirebaseUser?): String? = firebaseUser?.uid
}
