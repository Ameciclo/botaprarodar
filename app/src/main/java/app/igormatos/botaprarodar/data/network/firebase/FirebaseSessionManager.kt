package app.igormatos.botaprarodar.data.network.firebase

import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import kotlinx.coroutines.tasks.await

class FirebaseSessionManager(
    private val firebaseAuthModule: FirebaseAuthModule,
    private val sharedPreferencesModule: SharedPreferencesModule
) {

    suspend fun fetchAuthToken(): String? {
        val currentToken = fetchAuthTokenFromSession()

        if (currentToken == null) {
            updateSessionAuthToken()
            return fetchAuthTokenFromSession()
        }

        return currentToken
    }

    private fun fetchAuthTokenFromSession(): String? {
        return sharedPreferencesModule.getAuthToken()
    }

    private suspend fun updateSessionAuthToken() {
        val currentUser = firebaseAuthModule.getCurrentUser()

        if (currentUser != null) {
            val authToken = getAuthToken(currentUser)
            saveAuthToken(authToken)
        } else {
            throw UserAdminErrorException.AdminAccountNotFound
        }
    }

    private suspend fun getAuthToken(currentUser: FirebaseUser): String? {
        val result = getIdTokenForUser(currentUser)
        return result.token
    }


    private suspend fun getIdTokenForUser(user: FirebaseUser): GetTokenResult {
        return user.getIdToken(false).await()
    }

    private fun saveAuthToken(token: String?) {
       sharedPreferencesModule.saveAuthToken(token)
    }

}