package app.igormatos.botaprarodar.presentation.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.main.MainActivity
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        observeEvents()
        viewModel.verifyUserLoginState()
    }

    private fun observeEvents() {
        viewModel.userloginState.observe(this) { userLoginState ->
            val intent = when (userLoginState) {
                SplashViewModel.UserLoginState.LoggedIn -> MainActivity.getStartIntent(this)
                SplashViewModel.UserLoginState.PartiallyLoggedIn,
                SplashViewModel.UserLoginState.NotLoggedIn -> WelcomeActivity.getStartIntent(this)
            }
            startActivity(intent)
            finish()
        }
    }
}