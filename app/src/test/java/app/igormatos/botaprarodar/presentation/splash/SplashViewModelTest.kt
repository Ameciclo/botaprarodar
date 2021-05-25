package app.igormatos.botaprarodar.presentation.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelperModule
import app.igormatos.botaprarodar.domain.model.community.Community
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class SplashViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val preferencesModule = mockk<SharedPreferencesModule>()
    private val firebaseAuthModule = mockk<FirebaseAuthModule>()
    private val firebaseHelperModule = mockk<FirebaseHelperModule>(relaxed = true)

    private lateinit var viewmodel: SplashViewModel

    @Before
    fun setup() {
        viewmodel = SplashViewModel(preferencesModule, firebaseAuthModule, firebaseHelperModule)
    }

    @Test
    fun `should return NotLoggedIn when there is no current user in firebaseAuthModule`() {
        // arrange
        val expectedUserLoginResult = SplashViewModel.UserLoginState.NotLoggedIn
        every {
            firebaseAuthModule.getCurrentUser()
        } returns null

        // action
        viewmodel.verifyUserLoginState()
        val userLoginState: SplashViewModel.UserLoginState? = viewmodel.userloginState.value

        // assert
        assertEquals(userLoginState, expectedUserLoginResult)
    }

    @Test
    fun `should return PartiallyLoggedIn when user is LoggedIn but Community is not selected`() {
        // arrange
        val expectedUserLoginResult = SplashViewModel.UserLoginState.PartiallyLoggedIn
        every {
            firebaseAuthModule.getCurrentUser()
        } returns mockk()

        every {
            preferencesModule.isCommunitySelected()
        } returns false

        // action
        viewmodel.verifyUserLoginState()
        val userLoginState: SplashViewModel.UserLoginState? = viewmodel.userloginState.value

        // assert
        assertEquals(userLoginState, expectedUserLoginResult)
    }

    @Test
    fun `should return LoggedIn when user is LoggedIn and Community is selected`() {
        // arrange
        val expectedUserLoginResult = SplashViewModel.UserLoginState.LoggedIn
        every {
            firebaseAuthModule.getCurrentUser()
        } returns mockk()

        every {
            preferencesModule.isCommunitySelected()
        } returns true

        every {
            preferencesModule.getJoinedCommunity()
        } returns Community()

        // action
        viewmodel.verifyUserLoginState()
        val userLoginState: SplashViewModel.UserLoginState? = viewmodel.userloginState.value

        // assert
        assertEquals(userLoginState, expectedUserLoginResult)
    }
}