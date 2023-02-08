package app.igormatos.botaprarodar.presentation.splash

import android.content.Intent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.presentation.login.LoginViewModel
import app.igormatos.botaprarodar.presentation.login.selectCommunity.SelectCommunityViewModel
import app.igormatos.botaprarodar.presentation.main.viewModel.HomeViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

fun splash(
    path: String,
    func: SplashRobot.() -> Unit
) {
    SplashRobot(path).apply { func() }
}

@OptIn(ExperimentalComposeUiApi::class)
class SplashRobot(path: String) {

    @RelaxedMockK
    private lateinit var splashViewModel: SplashViewModel

    init {
        MockKAnnotations.init(this)

        stopKoin()
        startKoin {
            modules(module {
                factory { splashViewModel }
                factory { mockk<SharedPreferencesModule>(relaxed = true) }
                viewModel { mockk<HomeViewModel>(relaxed = true) }
                viewModel { mockk<LoginViewModel>(relaxed = true) }
                viewModel { mockk<SelectCommunityViewModel>(relaxed = true) }
            })
        }

        navigate(path)

        val intent = Intent(ApplicationProvider.getApplicationContext(), SplashActivity::class.java)

        ActivityScenario.launch<SplashActivity>(intent)
    }

    private fun navigate(path: String) {
        val mutableLiveData = MutableLiveData<SplashViewModel.UserLoginState>().apply {
            value = when(path) {
                HOME -> SplashViewModel.UserLoginState.LoggedIn
                SELECT_COMMUNITY -> SplashViewModel.UserLoginState.PartiallyLoggedIn
                else -> SplashViewModel.UserLoginState.LoggedOut
            }
        }

        every { splashViewModel.userLoginState } returns mutableLiveData
    }

    companion object {
        const val HOME = "HOME"
        const val SELECT_COMMUNITY = "SELECT_COMMUNITY"
        const val LOGIN = "LOGIN"
    }
}