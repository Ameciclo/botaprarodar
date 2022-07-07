package app.igormatos.botaprarodar.data.network.firebase

import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await


class FirebaseSessionManager(
    private val sharedPreferencesModule: SharedPreferencesModule,
    private val firebaseAuth: FirebaseAuth
) {
    fun fetchAuthToken(): String? {
        return fetchAuthTokenFromSession() ?: fetchAuthTokenFromApi()
    }

    fun fetchAuthTokenFromApi(): String? {
        runBlocking { updateSessionAuthToken() }
        return fetchAuthTokenFromSession()
    }

    fun shouldRenewToken(): Boolean {
        return sharedPreferencesModule.getAuthTokenRenovationStatus()
    }

    fun saveRenewStatusToken(shouldRenew: Boolean) {
        sharedPreferencesModule.saveAuthTokenRenovationStatus(shouldRenew)
    }

    private fun fetchAuthTokenFromSession(): String? {
        return sharedPreferencesModule.getAuthToken()
    }

    private suspend fun updateSessionAuthToken() {
        val currentUser = firebaseAuth.currentUser

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
        return user.getIdToken(true).await()
    }

    private fun saveAuthToken(token: String?) {
        sharedPreferencesModule.saveAuthToken(token)
    }
}