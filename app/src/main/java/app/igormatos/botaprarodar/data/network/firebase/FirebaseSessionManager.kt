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
        val currentToken = fetchAuthTokenFromSession()

        if (currentToken == null) {
            runBlocking { updateSessionAuthToken() }
            return fetchAuthTokenFromSession()
        }

        return currentToken
    }

    private fun fetchAuthTokenFromSession(): String? {
        return sharedPreferencesModule.getAuthToken()
    }

    private suspend fun updateSessionAuthToken() {
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            val authToken = getAuthToken(currentUser)
            saveAuthToken(authToken)
            addAuthTokenListener()
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

    private val authTokenListener = FirebaseAuth.IdTokenListener {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            saveRefreshAuthTokenInBackground(currentUser)
        } else {
            removeAuthTokenListener()
        }
    }

    private fun saveRefreshAuthTokenInBackground(currentUser: FirebaseUser) {
        currentUser.getIdToken(true).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val newAuthToken = task.result?.token
                saveAuthToken(newAuthToken)
            } else {
                saveAuthToken(null)
            }
        }
    }

    private fun addAuthTokenListener() {
        firebaseAuth.addIdTokenListener(authTokenListener)
    }

    private fun removeAuthTokenListener() {
        firebaseAuth.removeIdTokenListener(authTokenListener)
    }

}