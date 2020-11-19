package app.igormatos.botaprarodar.network

import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthModule {

    fun getCurrentUser(): FirebaseUser?
    fun signOut()
}