package app.igormatos.botaprarodar.data.network

import app.igormatos.botaprarodar.BuildConfig
import app.igormatos.botaprarodar.data.network.firebase.FirebaseSessionManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
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
        val originalRequest = chain.request()
        val httpUrl = originalRequest.url()
        val currentToken = firebaseSessionManager.fetchAuthToken()
        val authHttpUrl = httpUrl.newBuilder().addQueryParameter("auth", currentToken).build()
        val requestBuilder = originalRequest.newBuilder().url(authHttpUrl)
        val authRequest = requestBuilder.build()
        return chain.proceed(authRequest)
    }
}