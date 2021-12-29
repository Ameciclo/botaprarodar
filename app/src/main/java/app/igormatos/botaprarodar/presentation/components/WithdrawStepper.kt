package app.igormatos.botaprarodar.presentation.components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.adapter.WithdrawStepper
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import app.igormatos.botaprarodar.presentation.components.ui.theme.ColorPalet

//class WithdrawStepper : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            BotaprarodarTheme {
//                Surface(color = MaterialTheme.colors.background) {
//                    WithdrawStepperComponent(stringResource(id = R.string.borrow_bike))
//                }
//            }
//        }
//    }
//}

@Composable
fun WithdrawStepperComponent(title:String) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .height(120.dp)
                .padding(top = dimensionResource(id = R.dimen.padding_minimun))
                .background(Color.White)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Divider()
                Text(
                    modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)),
                    text = stringResource(id = R.string.borrow_bike)
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ItemLineSetStepperComponent(
                        IconStyle(
                            icon = painterResource(id = R.drawable.ic_bike),
                            iconColor = colorResource(id = R.color.green_teal),
                            lineColor = colorResource(id = R.color.green_teal)
                        )
                    )
                    ItemLineSetStepperComponent(
                        IconStyle(icon = painterResource(id = R.drawable.ic_user_step_icon))
                    )
                    ItemStepperComponent(IconStyle(icon = painterResource(id = R.drawable.ic_confirm)))
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(ColorPalet.BackgroundGray)
        ) {
            Divider()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WithdrawStepperPreview() {
    BotaprarodarTheme {
        WithdrawStepperComponent(stringResource(id = R.string.borrow_bike))
    }
}