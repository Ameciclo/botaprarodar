package app.igormatos.botaprarodar.data.network.firebase

import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FirebaseSessionManagerTest {
    private val firebaseAuth = mockk<FirebaseAuth>()
    private val sharedPreferencesModule = mockk<SharedPreferencesModule>()
    private val validToken = "test"
    private lateinit var firebaseSessionManager: FirebaseSessionManager

    @BeforeEach
    fun setUp() {
        firebaseSessionManager = FirebaseSessionManager(sharedPreferencesModule, firebaseAuth)
    }

    @Test
    fun `should return shared prefs token when it is not null`() {
        every { sharedPreferencesModule.getAuthToken() } returns validToken

        val authToken = runBlocking { firebaseSessionManager.fetchAuthToken() }

        assertThat(authToken, equalTo(validToken))
    }


    @Test
    fun `should return valid new token when shared prefs token is initially null`() {
        every { sharedPreferencesModule.getAuthToken() } returns null andThen validToken
        every { sharedPreferencesModule.saveAuthToken(any()) } returns Unit
        val mockedTokenResult = mockFirebaseTokenResult()
        val mockedFirebaseTokenTaskResult = mockFirebaseTokenTaskResult(mockedTokenResult)
        mockFirebaseTokenTaskResultListener(mockedFirebaseTokenTaskResult)
        mockFirebaseCurrentUser(mockedFirebaseTokenTaskResult)
        mockFirebaseTokenListeners()

        val authToken = runBlocking { firebaseSessionManager.fetchAuthToken() }

        assertThat(authToken, equalTo(validToken))
    }

    private fun mockFirebaseTokenListeners() {
        every { firebaseAuth.addIdTokenListener(any<FirebaseAuth.IdTokenListener>()) } returns Unit
        every { firebaseAuth.removeIdTokenListener(any<FirebaseAuth.IdTokenListener>()) } returns Unit
    }

    private fun mockFirebaseCurrentUser(mockedFirebaseTokenTaskResult: Task<GetTokenResult>) {
        every { firebaseAuth.currentUser } returns mockk()
        val anyFirebaseUser: FirebaseUser = mockk()
        every { anyFirebaseUser.getIdToken(any()) } returns mockedFirebaseTokenTaskResult
        every { firebaseAuth.currentUser?.getIdToken(any()) } returns mockedFirebaseTokenTaskResult
    }

    private fun mockFirebaseTokenTaskResultListener(mockedInstanceId: Task<GetTokenResult>) {
        val slot = slot<OnCompleteListener<GetTokenResult>>()
        every { mockedInstanceId.addOnCompleteListener(capture(slot)) } answers {
            slot.captured.onComplete(mockedInstanceId)
            mockedInstanceId
        }
    }

    private fun mockFirebaseTokenTaskResult(mockedResult: GetTokenResult): Task<GetTokenResult> {
        val mockedInstanceId = mockk<Task<GetTokenResult>>()
        every { mockedInstanceId.isSuccessful } returns true
        every { mockedInstanceId.result } returns mockedResult
        every { mockedInstanceId.isComplete } returns true
        every { mockedInstanceId.exception } returns null
        every { mockedInstanceId.isCanceled } returns false
        return mockedInstanceId
    }

    private fun mockFirebaseTokenResult(): GetTokenResult {
        val mockedResult = mockk<GetTokenResult>()
        every { mockedResult.token } returns validToken
        return mockedResult
    }
}