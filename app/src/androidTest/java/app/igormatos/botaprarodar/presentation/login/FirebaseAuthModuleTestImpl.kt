package app.igormatos.botaprarodar.presentation.login

import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModule
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthModuleTestImpl(
    private val user: FirebaseUser
) : FirebaseAuthModule {

    override fun getCurrentUser(): FirebaseUser {
        return user
    }

    override fun signOut() {

    }
}