package app.igormatos.botaprarodar.presentation.returnbicycle

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.components.FinishActionComponent
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import app.igormatos.botaprarodar.presentation.main.HomeActivity

@ExperimentalComposeUiApi
class FinishReturnBikeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BotaprarodarTheme {
                Surface(color = MaterialTheme.colors.background) {
                    FinishActionComponent(
                        mainMessage = stringResource(id = R.string.success_devolution_message),
                        mainActionText = stringResource(id = R.string.repeat_devolution_title),
                        backToHome = { backToHome() },
                        mainAction = { returnAnotherBike() }
                    )
                }
            }
        }
    }

    private fun backToHome() {
        val intentHome = HomeActivity.getStartIntent(this)
        startActivity(intentHome)
        finish()
    }


    private fun returnAnotherBike() {
        val intent = Intent(this, ReturnBicycleActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    BotaprarodarTheme {
        FinishActionComponent(
            mainMessage = stringResource(id = R.string.success_devolution_message),
            mainActionText = stringResource(id = R.string.repeat_devolution_title),
            backToHome = { },
            mainAction = { }
        )
    }
}