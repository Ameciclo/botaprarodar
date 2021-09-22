package app.igormatos.botaprarodar.presentation.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.login.LoginActivity
import app.igormatos.botaprarodar.presentation.login.selectCommunity.SelectCommunityActivity
import app.igormatos.botaprarodar.presentation.main.MainActivity
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
                SplashViewModel.UserLoginState.LoggedIn ->
                    MainActivity.getStartIntent(this)
                SplashViewModel.UserLoginState.PartiallyLoggedIn ->
                    SelectCommunityActivity.getStartIntent(this)
                SplashViewModel.UserLoginState.NotLoggedIn ->
                    LoginActivity.getStartIntent(this)
            }
            navigateToActivity(intent)
        }
    }

    private fun navigateToActivity(intent: Intent) {
        startActivity(intent)
        finish()
//        overridePendingTransition(
//            R.anim.slide_in_bottom,
//            R.anim.slide_out_top
//        )
    }
}