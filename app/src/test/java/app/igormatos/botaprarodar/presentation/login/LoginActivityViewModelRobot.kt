package app.igormatos.botaprarodar.presentation.login

import app.igormatos.botaprarodar.data.network.*
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.domain.model.community.Community
import com.brunotmgomes.ui.extensions.getOrAwaitValue
import io.mockk.*
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertThat

class LoginActivityViewModelRobot private constructor(
    private var userLoggedIn: Boolean = true,
    private val userChoseCommunity: Boolean = true,
    private val emailIsVerified: Boolean = true,
) {

    companion object {
        fun test(
            userLoggedIn: Boolean = true,
            userChoseCommunity: Boolean = true,
            emailIsVerified: Boolean = true,
            block: LoginActivityViewModelRobot.() -> Unit,
        ) =
            LoginActivityViewModelRobot(
                userLoggedIn,
                userChoseCommunity,
                emailIsVerified,
            ).apply {
                block()
                finish()
            }
    }

    private val viewModel: LoginActivityViewModelImpl

    private val preferencesModule: SharedPreferencesModule
    private val firebaseAuthModule: FirebaseAuthModule
    private val firebaseHelperModule: FirebaseHelperModule

    private val firebaseListenerSlot: CapturingSlot<SingleRequestListener<Pair<Boolean, List<Community>>>> =
        slot()
    private val saveCommunityFirebaseSlot: CapturingSlot<Community> = slot()
    private val saveCommunitySharedPrefsSlot: CapturingSlot<String> = slot()

    val mockChosenCommunityId = "testId"
    val mockUserId = "abcd"
    val mockEmail = "abcd@a.com"

    init {
        firebaseAuthModule = mockk {
            every { getCurrentUser() } answers {
                if (userLoggedIn) {
                    mockk {
                        every { uid } returns mockUserId
                        every { email } returns mockEmail
                        every { isEmailVerified } returns emailIsVerified
                    }
                } else {
                    null
                }
            }
            every { signOut() } just (runs)
        }

        preferencesModule = mockk {
            every { isCommunitySelected() } answers {
                userChoseCommunity
            }
            every { getJoinedCommunity() } returns mockk {
                every { id } returns mockChosenCommunityId
            }
            every { saveJoinedCommmunity(capture(saveCommunityFirebaseSlot)) } just (runs)
        }

        firebaseHelperModule = mockk {
            every { setCommunityId(capture(saveCommunitySharedPrefsSlot)) } just (runs)
            every { getCommunities(any(), any(), capture(firebaseListenerSlot)) } just (runs)
        }

        viewModel =
            LoginActivityViewModelImpl(
                preferencesModule,
                firebaseAuthModule,
                firebaseHelperModule
            )
    }

    fun triggerCheckPreviousState() {
        viewModel.checkPreviousState()
    }

    fun triggerReloadCommunities(){
        viewModel.retryLoadCommunities()
    }

    fun startCommunitiesRequest() {
        val listener = firebaseListenerSlot.captured
        listener.onStart()
    }

    fun login(
        success: Boolean = true
    ) {
        if (success) {
            userLoggedIn = true
            viewModel.onUserLoggedIn()
        }
    }

    fun finishCommunitiesRequest(
        success: Boolean = true,
        isAdmin: Boolean = true,
        communities: List<Community> = emptyList(),
        error: RequestError = RequestError.DEFAULT
    ) {
        val listener = firebaseListenerSlot.captured
        if (success) {
            listener.onCompleted(Pair(isAdmin, communities))
        } else {
            listener.onError(error)
        }
    }

    fun chooseCommunity(chosenCommunity: Community) {
        viewModel.chooseCommunity(chosenCommunity)
    }

    fun verifyLoggedOut(){
        verify { firebaseAuthModule.signOut() }
    }

    fun verifyTriggeredShowResendEmail() {
        val event = viewModel.showResendEmailSnackBar.getOrAwaitValue()
        val content = event.getContentIfNotHandled()
        assertThat(content, `is`(true))
    }

    fun verifyTriggeredLoadCommunities() {
        verify { firebaseHelperModule.getCommunities(any(), any(), any()) }
    }

    fun verifyLoading(expectedLoading: Boolean) {
        val loading = viewModel.loading.getOrAwaitValue()
        assertThat(loading, `is`(expectedLoading))
    }

    fun verifyLoadedCommunities(expectedAdmin: Boolean, expectedCommunities: List<Community>) {
        val event = viewModel.loadedCommunities.getOrAwaitValue()
        val loadedData = event.getContentIfNotHandled()?.getOrNull()
        assertThat(loadedData?.communities, `is`(expectedCommunities))
        assertThat(loadedData?.isAdmin, `is`(expectedAdmin))
    }

    fun verifyLoadedCommunitiesFailed(){
        val event = viewModel.loadedCommunities.getOrAwaitValue()
        val loadedData: Throwable? = event.getContentIfNotHandled()?.exceptionOrNull()
        assertThat(loadedData, notNullValue())
    }

    // verified whether the chosen community was saved to firebase and shared preferences
    fun verifySavedCommunity(expectedCommunity: Community) {
        val savedCommunityFirebase = saveCommunityFirebaseSlot.captured
        val savedCommunitySharedPrefs = saveCommunitySharedPrefsSlot.captured
        assertThat(savedCommunityFirebase, `is`(expectedCommunity))
        assertThat(savedCommunitySharedPrefs, `is`(expectedCommunity.id))
    }

    fun verifyNavigatedToMainActivity() {
        val event = viewModel.navigateMain.getOrAwaitValue()
        val content = event.getContentIfNotHandled()
        assertThat(content, `is`(true))
    }

    private fun finish() {}
}