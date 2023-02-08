package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BicycleReturnUseType
import app.igormatos.botaprarodar.domain.model.Quiz
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.GetNeighborhoodsUseCase.Companion.OTHER_NEIGHBORHOOD
import app.igormatos.botaprarodar.presentation.components.ui.theme.ColorPallet
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@Composable
fun ReturnBicycleQuizPage(handleClick: (Any?) -> Unit, viewModel: ReturnBicycleViewModel) {
    var reason by remember { mutableStateOf("") }
    var neighborhood by remember { mutableStateOf("") }
    var customNeighborhood by remember { mutableStateOf("") }
    var hasIssues by remember { mutableStateOf("Não") }
    var gaveRide by remember { mutableStateOf("Não") }
    val customNeighborhoodSelected by derivedStateOf { neighborhood == OTHER_NEIGHBORHOOD }

    val purposesOfTheBicycle = viewModel.loadBicycleReturnUseArray()
    val neighborhoods by viewModel.neighborhoods.observeAsState(emptyArray())
    val validNeighborhoods = neighborhoods.filterNot { it == OTHER_NEIGHBORHOOD }

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

            QuizDropDownMenu(
                title = stringResource(id = R.string.return_bike_used_bike_to_move),
                items = purposesOfTheBicycle
            ) {
                reason = it
            }

            QuizDropDownMenu(
                title = stringResource(id = R.string.return_bike_neighborhood_hint),
                items = neighborhoods
            ) {
                neighborhood = it
            }

            Spacer(Modifier.height(5.dp))

            AnimatedVisibility(
                visible = customNeighborhoodSelected,
                enter = fadeIn(initialAlpha = 0.4f),
                exit = fadeOut(animationSpec = tween(durationMillis = 100))
            ) {
                Column {
                    QuizTextField(
                        title = stringResource(id = R.string.return_bike_write_neighborhood_hint)
                    ) {
                        customNeighborhood = it
                    }
                }
            }

            QuizYesOrNoRadioGroup(title = stringResource(id = R.string.problems_during_riding)) {
                hasIssues = it
            }

            QuizYesOrNoRadioGroup(title = stringResource(id = R.string.need_take_ride)) {
                gaveRide = it
            }

            Spacer(Modifier.weight(1f))

            val hasValidData = reason.isNotEmpty()
                    && (validNeighborhoods.contains(neighborhood) || customNeighborhood.isNotEmpty())
                    && hasIssues.isNotEmpty()
                    && gaveRide.isNotEmpty()

            Button(
                enabled = hasValidData,
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.padding_medium))
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.height_48)),
                onClick = {
                    val selectedNeighborhood = neighborhood.takeUnless { it == OTHER_NEIGHBORHOOD } ?: customNeighborhood
                    val useTripType = BicycleReturnUseType.getBicycleReturnUseTypeByValue(reason)
                    handleClick(Quiz(selectedNeighborhood, useTripType?.index, hasIssues, gaveRide))
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.green_teal))
            ) {
                Text(
                    text = stringResource(id = R.string.confirm_return),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.gray_1)
                )
            }
        }
    }

}

@Composable
private fun QuizYesOrNoRadioGroup(title: String, onSelectedItem: (String) -> Unit) {
    var selected by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(
                top = dimensionResource(id = R.dimen.padding_medium),
                bottom = dimensionResource(id = R.dimen.padding_small)
            ),
            text = title,
            style = TextStyle(color = ColorPallet.TextGray, fontSize = 14.sp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            val yes = stringResource(id = R.string.yes)
            val no = stringResource(id = R.string.no)
            RadioButton(
                selected = selected,
                onClick = {
                    selected = !selected
                    onSelectedItem(yes)
                }, colors = RadioButtonDefaults.colors(
                    selectedColor = colorResource(id = R.color.green_teal)
                )
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = yes,
                style = TextStyle(color = ColorPallet.TextGray, fontWeight = FontWeight(500))
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)))
            RadioButton(
                selected = !selected,
                onClick = {
                    selected = !selected
                    onSelectedItem(no)
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = colorResource(id = R.color.green_teal)
                )
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = no,
                style = TextStyle(color = ColorPallet.TextGray, fontWeight = FontWeight(500))
            )
        }
    }

}

@ExperimentalComposeUiApi
@Composable
private fun QuizTextField(title: String, onValueChanged: (String) -> Unit) {
    var neighborhoodName by rememberSaveable { mutableStateOf(String()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Text(
        modifier = Modifier.padding(
            top = dimensionResource(id = R.dimen.padding_medium),
            bottom = dimensionResource(id = R.dimen.padding_small)
        ),
        text = title,
        style = TextStyle(color = ColorPallet.TextGray)
    )
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = neighborhoodName,
        onValueChange = {
            neighborhoodName = it
            onValueChanged(neighborhoodName)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFFD8D8D8),
            unfocusedBorderColor = Color(0xFFD8D8D8)
        ),
        placeholder = {
            Text(
                text = "Digite o destino",
                style = TextStyle(
                    color = ColorPallet.TextGray,
                    fontWeight = FontWeight(500)
                )
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        })
    )
}

@Composable
private fun QuizDropDownMenu(
    title: String,
    items: Array<String>,
    onSelectedItem: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val select = stringResource(id = R.string.return_bike_survey_select)
    var currentItem by remember { mutableStateOf(select) }
    var dropMenuSize by remember { mutableStateOf(Size.Zero) }
    val rotateIconAnimation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(
                top = dimensionResource(id = R.dimen.padding_medium),
                bottom = dimensionResource(id = R.dimen.padding_small)
            ),
            text = title,
            style = TextStyle(color = ColorPallet.TextGray)
        )

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .onGloballyPositioned { coordinates ->
                    dropMenuSize = coordinates.size.toSize()
                },
            colors = ButtonDefaults.buttonColors(backgroundColor = ColorPallet.BackgroundGray),
            onClick = { expanded = true },
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = currentItem,
                    style = TextStyle(
                        color = ColorPallet.TextGray,
                        fontWeight = FontWeight(500)
                    )
                )
                Spacer(modifier = Modifier.width(1.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = stringResource(R.string.icon_arrow_down_description),
                    tint = ColorPallet.TextGray,
                    modifier = Modifier.graphicsLayer { rotationZ = rotateIconAnimation }
                )
            }
        }

        DropdownMenu(
            modifier = Modifier.width(with(LocalDensity.current) { dropMenuSize.width.toDp() }),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onSelectedItem(item)
                    currentItem = item
                    expanded = false
                }) {
                    Text(text = item)
                }
            }
        }
    }
}
