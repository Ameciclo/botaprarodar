package app.igormatos.botaprarodar.presentation.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.ExperimentalComposeUiApi
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.login.signin.LoginActivity
import app.igormatos.botaprarodar.presentation.login.selectCommunity.SelectCommunityActivity
import app.igormatos.botaprarodar.presentation.main.HomeActivity
import app.igormatos.botaprarodar.presentation.splash.SplashViewModel.UserLoginState.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel.userLoginState.observe(this) {
            val intent = when (it) {
                LoggedIn -> HomeActivity.getStartIntent(this)
                PartiallyLoggedIn -> SelectCommunityActivity.getStartIntent(this)
                LoggedOut -> LoginActivity.getStartIntent(this)
            }

            navigateToActivity(intent)
        }
    }

    private fun navigateToActivity(intent: Intent) {
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top)
    }
}