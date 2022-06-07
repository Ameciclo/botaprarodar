package app.igormatos.botaprarodar.presentation.bikewithdraw

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.components.FinishActionComponent
import app.igormatos.botaprarodar.presentation.components.WithdrawStepper
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import app.igormatos.botaprarodar.presentation.main.HomeActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
class FinishWithdrawActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BotaprarodarTheme {
                Surface(color = MaterialTheme.colors.background) {
                    FinishActionComponent(
                        mainMessage = stringResource(id = R.string.success_withdraw_message),
                        mainActionText = stringResource(id = R.string.repeat_withdraw_title),
                        backToHome = { backToHome() },
                        mainAction = { withdrawAnotherBike() }
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        backToHome()
    }

    private fun backToHome() {
        val intentHome  = HomeActivity.getStartIntent(this)
        startActivity(intentHome)
        finish()
    }

    private fun withdrawAnotherBike() {
        val intentWithdraw = Intent(this, WithdrawStepper::class.java)
        startActivity(intentWithdraw)
        finish()
    }
}
