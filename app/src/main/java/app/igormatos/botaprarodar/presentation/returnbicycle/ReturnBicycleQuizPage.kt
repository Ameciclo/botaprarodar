package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import app.igormatos.botaprarodar.presentation.components.ui.theme.ColorPallet

@Composable
fun ReturnBicycleQuizPage() {
    val usedBikeToMoveList = stringArrayResource(id = R.array.used_bike_to_move_list)
    var usedBikeToMoveSelected by
    remember { mutableStateOf("Selecione") }
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .background(ColorPallet.BackgroundGray)
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium))) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(R.string.return_bicycle_quiz_page_answer_quiz))
            }

            Text(
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.padding_large),
                    bottom = dimensionResource(id = R.dimen.padding_small)
                ),
                text = stringResource(id = R.string.used_bike_to_move),
                style = TextStyle(color = ColorPallet.TextGray)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .wrapContentSize(Alignment.TopStart)
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = ColorPallet.BackgroundGray),
                    onClick = { expanded = true },
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = usedBikeToMoveSelected,
                            style = TextStyle(
                                color = ColorPallet.TextGray,
                                fontWeight = FontWeight(500)
                            )
                        )
                        Spacer(modifier = Modifier.width(1.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_down),
                            contentDescription = stringResource(R.string.icon_arrow_down_description),
                            tint = ColorPallet.TextGray
                        )
                    }
                }

                Box(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium))) {
                    DropdownMenu(
                        modifier = Modifier
                            .fillMaxWidth(),
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        usedBikeToMoveList.forEach { item ->
                            DropdownMenuItem(onClick = {
                                usedBikeToMoveSelected = item
                                expanded = false
                            }) {
                                Text(text = item)
                            }
                        }
                    }
                }
            }
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