package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import app.igormatos.botaprarodar.presentation.components.ui.theme.ColorPallet

@Composable
fun ReturnBicycleQuizPage() {
    Column(modifier = Modifier.background(ColorPallet.BackgroundGray)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(R.string.return_bicycle_quiz_page_answer_quiz))
        }
    }

}

@Preview(showSystemUi = true)
@Composable
private fun ReturnBicycleQuizPagePreview() {
    BotaprarodarTheme {
        ReturnBicycleQuizPage()
    }
}