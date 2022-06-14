package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.common.enumType.StepConfigType.*
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import app.igormatos.botaprarodar.presentation.components.ui.theme.ColorPallet

@Composable
fun ThreeStepper(uiStepConfig: StepConfigType?, secondIcon: Painter) {
    var iconStyleFirst = IconStyle(
        icon = painterResource(id = R.drawable.ic_bike),
        iconColor = ColorPallet.GreenTeal,
        lineColor = ColorPallet.GreenTeal,
    )
    var iconStyleSecond = IconStyle(icon = secondIcon)
    var iconStyleThird = IconStyle(icon = painterResource(id = R.drawable.ic_confirm))


    when (uiStepConfig) {
        SELECT_USER, QUIZ -> {
            iconStyleFirst = iconStyleFirst.copy(
                backgroundColor = ColorPallet.GreenTeal,
                iconColor = Color.White
            )
            iconStyleSecond = iconStyleSecond.copy(
                iconColor = ColorPallet.GreenTeal,
                lineColor = ColorPallet.GreenTeal
            )
        }
        CONFIRM_WITHDRAW, CONFIRM_DEVOLUTION -> {
            iconStyleFirst = iconStyleFirst.copy(
                backgroundColor = ColorPallet.GreenTeal,
                iconColor = Color.White
            )

            iconStyleSecond = iconStyleSecond.copy(
                iconColor = Color.White,
                lineColor = ColorPallet.GreenTeal,
                backgroundColor = ColorPallet.GreenTeal,
            )

            iconStyleThird = iconStyleThird.copy(
                iconColor = ColorPallet.GreenTeal,
                lineColor = ColorPallet.GreenTeal
            )
        }
        FINISHED_ACTION -> {
            iconStyleFirst = iconStyleFirst.copy(
                backgroundColor = ColorPallet.GreenTeal,
                iconColor = Color.White
            )

            iconStyleSecond = iconStyleSecond.copy(
                iconColor = Color.White,
                lineColor = ColorPallet.GreenTeal,
                backgroundColor = ColorPallet.GreenTeal,
            )

            iconStyleThird = iconStyleThird.copy(
                iconColor = Color.White,
                lineColor = ColorPallet.GreenTeal,
                backgroundColor = ColorPallet.GreenTeal,
            )
        }
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        ItemLineSetStepperComponent(
            iconStyleFirst
        )
        ItemLineSetStepperComponent(
            iconStyleSecond
        )
        ItemStepperComponent(iconStyleThird)
    }
}

@Preview(showBackground = true)
@Composable
private fun ThreeStepperPreview() {
    BotaprarodarTheme {
        ThreeStepper(SELECT_USER, painterResource(id = R.drawable.ic_quiz))
    }
}