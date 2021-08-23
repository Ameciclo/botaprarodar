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
    fun `auth interceptor should fetch auth token`() {
        mockResponse()
        val request = mockRequest()
        val url = mockRequestUrls(request)
        mockBuilders(url)
        mockToken()

        authInterceptor.intercept(chain)

        verify { firebaseSessionManager.fetchAuthToken() }
    }

    private fun mockToken() {
        every { firebaseSessionManager.fetchAuthToken() } returns "test"
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

    private fun mockResponse() {
        val response: Response = mockk()
        every { chain.proceed(any()) } returns response
    }
}