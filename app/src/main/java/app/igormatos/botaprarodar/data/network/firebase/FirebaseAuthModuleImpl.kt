package app.igormatos.botaprarodar.data.network.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthModuleImpl : FirebaseAuthModule {
    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }


}