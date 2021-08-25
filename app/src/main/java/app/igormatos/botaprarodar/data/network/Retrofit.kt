package app.igormatos.botaprarodar.data.network

import app.igormatos.botaprarodar.BuildConfig
import app.igormatos.botaprarodar.data.network.firebase.FirebaseSessionManager
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun buildRetrofit(authInterceptor: AuthTokenInterceptor): Retrofit {
    val httpClientBuilder = OkHttpClient.Builder()

    configureLoggingInterceptor(httpClientBuilder)
    httpClientBuilder.addInterceptor(authInterceptor)

    val httpClient = httpClientBuilder.build()

    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}

private fun configureLoggingInterceptor(httpClientBuilder: OkHttpClient.Builder) {
    val logging = HttpLoggingInterceptor()

    if (BuildConfig.DEBUG) {
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(logging)
    }
}

class AuthTokenInterceptor(private val firebaseSessionManager: FirebaseSessionManager) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val invalidTokenError = 401
        val currentToken = firebaseSessionManager.fetchAuthToken()
        val originalRequest = chain.request()
        var response = proceedWithAuthRequest(originalRequest, currentToken, chain)

        if (response.code() == invalidTokenError) {
            if (firebaseSessionManager.shouldRenewToken()) {
                val newToken = firebaseSessionManager.fetchAuthTokenFromApi()
                // We try one more time with a new token. If it does not work, logout
                firebaseSessionManager.saveRenewStatusToken(shouldRenew = false)
                response.close()
                response = proceedWithAuthRequest(originalRequest, newToken, chain)
            } else {
                FirebaseAuth.getInstance().signOut()
            }
        } else {
            firebaseSessionManager.saveRenewStatusToken(shouldRenew = true)
        }

        return response;
    }

    private fun proceedWithAuthRequest(
        originalRequest: Request,
        currentToken: String?,
        chain: Interceptor.Chain
    ): Response {
        val authRequest = addAuthToRequest(originalRequest, currentToken)
        return chain.proceed(authRequest)
    }

    private fun addAuthToRequest(
        originalRequest: Request,
        currentToken: String?
    ): Request {
        val httpUrl = originalRequest.url()
        val authHttpUrl = httpUrl.newBuilder().addQueryParameter("auth", currentToken).build()
        val requestBuilder = originalRequest.newBuilder().url(authHttpUrl)
        return requestBuilder.build()
    }
}