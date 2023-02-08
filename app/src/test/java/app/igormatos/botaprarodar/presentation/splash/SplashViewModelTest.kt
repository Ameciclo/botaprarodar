package app.igormatos.botaprarodar.presentation.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelperModule
import app.igormatos.botaprarodar.domain.model.community.Community
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

internal class SplashViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val preferencesModule = mockk<SharedPreferencesModule>()
    private val firebaseAuthModule = mockk<FirebaseAuthModule>()
    private val firebaseHelperModule = mockk<FirebaseHelperModule>(relaxed = true)

    private lateinit var viewModel: SplashViewModel

    @Test
    fun `should return LoggedOut when there is no current user in firebaseAuthModule`() {
        // arrange
        val expectedUserLoginResult = SplashViewModel.UserLoginState.LoggedOut

        every { firebaseAuthModule.getCurrentUser() } returns null

        // action
        viewModel = SplashViewModel(preferencesModule, firebaseAuthModule, firebaseHelperModule)
        val userLoginState: SplashViewModel.UserLoginState? = viewModel.userLoginState.value

        // assert
        assertEquals(userLoginState, expectedUserLoginResult)
    }

    @Test
    fun `should return PartiallyLoggedIn when user is LoggedIn but Community is not selected`() {
        // arrange
        val expectedUserLoginResult = SplashViewModel.UserLoginState.PartiallyLoggedIn

        every { firebaseAuthModule.getCurrentUser() } returns mockk()
        every { preferencesModule.isCommunitySelected() } returns false

        // action
        viewModel = SplashViewModel(preferencesModule, firebaseAuthModule, firebaseHelperModule)
        val userLoginState: SplashViewModel.UserLoginState? = viewModel.userLoginState.value

        // assert
        assertEquals(userLoginState, expectedUserLoginResult)
    }

    @Test
    fun `should return LoggedIn when user is LoggedIn and Community is selected`() {
        // arrange
        val expectedUserLoginResult = SplashViewModel.UserLoginState.LoggedIn

        every { firebaseAuthModule.getCurrentUser() } returns mockk()
        every { preferencesModule.isCommunitySelected() } returns true
        every { preferencesModule.getJoinedCommunity() } returns Community()

        // action
        viewModel = SplashViewModel(preferencesModule, firebaseAuthModule, firebaseHelperModule)
        val userLoginState: SplashViewModel.UserLoginState? = viewModel.userLoginState.value

        // assert
        assertEquals(userLoginState, expectedUserLoginResult)
    }
}