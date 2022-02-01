package app.igormatos.botaprarodar.presentation.components

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class FinishAction : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BotaprarodarTheme {
                Surface(color = MaterialTheme.colors.background) {
                    FinishActionComponent(
                        mainMessage = stringResource(id = R.string.success_withdraw_message),
                        mainActionText = stringResource(id = R.string.repeat_withdraw_title),
                        mainAction = {
                            startActivity(Intent(this, WithdrawStepper::class.java))
                        },
                        backToHome = { finish() })
                }
            }
        }
    }
}

@Composable
fun FinishActionComponent(
    mainMessage: String,
    mainActionText: String,
    mainAction: (Any?) -> Unit,
    backToHome: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(
                colorResource(id = R.color.background_card_user_item_gray)
            )
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_xxlarge)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_confirmation_step),
                contentDescription = "Confirmation Icon",
                modifier = Modifier
                    .size(220.dp)
                    .padding(bottom = dimensionResource(id = R.dimen.padding_large))
            )

            Text(
                text = mainMessage,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_xxlarge))
            )

            OutlinedButton(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                border = BorderStroke(1.dp, colorResource(id = R.color.colorPrimary)),
                modifier = Modifier
                    .padding(bottom = dimensionResource(id = R.dimen.margin_medium))
                    .fillMaxWidth(),
                onClick = { mainAction(null) }
            ) {
                Text(
                    text = mainActionText.uppercase(),
                    color = colorResource(id = R.color.colorPrimary),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp),
                )
            }

            TextButton(
                onClick = backToHome
            ) {
                Text(
                    text = stringResource(R.string.back_to_home),
                    color = colorResource(id = R.color.text_gray),
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DefaultPreview() {
    BotaprarodarTheme {
        FinishActionComponent(
            mainMessage = "Empréstimo realizado",
            mainActionText = "Emprestar outra bicicleta",
            {},
            {})
    }
}