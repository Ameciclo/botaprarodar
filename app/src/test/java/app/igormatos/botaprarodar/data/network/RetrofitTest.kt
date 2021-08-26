package app.igormatos.botaprarodar.data.network

import app.igormatos.botaprarodar.data.network.firebase.FirebaseSessionManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.jupiter.api.Test

class RetrofitTest {
    private val firebaseSessionManager = mockk<FirebaseSessionManager>()
    private val chain: Interceptor.Chain = mockk()
    private val authInterceptor = AuthTokenInterceptor(firebaseSessionManager)

    @Test
    fun `auth interceptor should fetch auth token when request`() {
        val httpSuccessStatusCode = 200
        mockAuthInterceptor(httpSuccessStatusCode)

        authInterceptor.intercept(chain)

        verify { firebaseSessionManager.fetchAuthToken() }
    }

    @Test
    fun `auth interceptor should fetch auth token from api when http code is unauthorized`() {
        val httpUnauthorizedStatusCode = 401
        mockAuthInterceptor(httpUnauthorizedStatusCode)

        authInterceptor.intercept(chain)

        verify { firebaseSessionManager.fetchAuthTokenFromApi() }
    }

    private fun mockAuthInterceptor(httpStatusCode: Int) {
        mockResponse(httpStatusCode)
        val request = mockRequest()
        val url = mockRequestUrls(request)
        mockBuilders(url)
        mockToken()
    }

    private fun mockToken() {
        every { firebaseSessionManager.fetchAuthToken() } returns "test"
        every { firebaseSessionManager.fetchAuthTokenFromApi() } returns "test"
        every { firebaseSessionManager.saveRenewStatusToken(any()) } returns Unit
        every { firebaseSessionManager.shouldRenewToken() } returns true
    }

    private fun mockBuilders(url: HttpUrl) {
        val httpBuilder: HttpUrl.Builder = mockk()
        every { url.newBuilder() } returns httpBuilder
        every { httpBuilder.addQueryParameter(any(), any()) } returns httpBuilder
        every { httpBuilder.build() } returns url
    }

    private fun mockRequestUrls(request: Request): HttpUrl {
        val url: HttpUrl = mockk()
        every { request.url() } returns url
        return url
    }

    private fun mockRequest(): Request {
        val request: Request = mockk()
        every { chain.request() } returns request
        val requestBuilder: Request.Builder = mockk()
        every { request.newBuilder() } returns requestBuilder
        every { requestBuilder.url(any<HttpUrl>()) } returns requestBuilder
        every { requestBuilder.build() } returns request
        return request
    }

    private fun mockResponse(httpStatusCode : Int) {
        val response: Response = mockk()
        every { chain.proceed(any()) } returns response
        every { response.code() } returns httpStatusCode
        every { response.close() } returns Unit
    }
}