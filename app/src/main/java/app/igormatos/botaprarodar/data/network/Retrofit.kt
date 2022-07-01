package app.igormatos.botaprarodar.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Singleton

fun buildRetrofit(
    authInterceptor: AuthTokenInterceptor,
    noConnectionInterceptor: NoConnectionInterceptor
): Retrofit {
    val httpClientBuilder = OkHttpClient.Builder()

    configureLoggingInterceptor(httpClientBuilder)
    httpClientBuilder.addInterceptor(authInterceptor)
    httpClientBuilder.addInterceptor(noConnectionInterceptor)

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
        logging.level = HttpLoggingInterceptor.Level.BASIC
        httpClientBuilder.addInterceptor(logging)
    }
}

class AuthTokenInterceptor(private val firebaseSessionManager: FirebaseSessionManager) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val httpUnauthorizedStatusCode = 401
        val currentToken = firebaseSessionManager.fetchAuthToken()
        val originalRequest = chain.request()
        var response = proceedWithAuthRequest(originalRequest, currentToken, chain)

        if (response.code() == httpUnauthorizedStatusCode) {
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

        return response
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

@Singleton
class NoConnectionInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!isConnectionOn()) {
            throw NoConnectivityException()
        } else if (!isInternetAvailable()) {
            throw NoInternetException()
        } else {
            chain.proceed(chain.request())
        }
    }

    private fun isConnectionOn(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val connection = connectivityManager.getNetworkCapabilities(network)
            return connection != null && (
                    connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            if (activeNetwork != null) {
                return (activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                        activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
            }
            return false
        }
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            val timeoutMs = 1500
            val sock = Socket()
            val sockaddr = InetSocketAddress("8.8.8.8", 53)

            sock.connect(sockaddr, timeoutMs)
            sock.close()

            true
        } catch (e: IOException) {
            false
        }

    }

    class NoConnectivityException : IOException() {
        override val message: String
            get() = "No network available, please check your WiFi or Data connection"
    }

    class NoInternetException : IOException() {
        override val message: String
            get() = "No internet available, please check your connected WIFi or Data"
    }
}