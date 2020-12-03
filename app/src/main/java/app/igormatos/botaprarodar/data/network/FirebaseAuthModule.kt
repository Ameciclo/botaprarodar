package app.igormatos.botaprarodar.data.network

import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthModule {

    fun getCurrentUser(): FirebaseUser?
    fun signOut()
}