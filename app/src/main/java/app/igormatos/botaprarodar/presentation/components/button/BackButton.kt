package app.igormatos.botaprarodar.presentation.components.button

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.igormatos.botaprarodar.R

@Composable
fun BackButtom(handleClick: () -> Unit) {
    IconButton(
        modifier = Modifier.width(112.dp),
        onClick = handleClick,
        content = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(Modifier.padding(dimensionResource(id = R.dimen.padding_small)))
                Icon(
                    modifier = Modifier
                        .size(16.dp),
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = "Voltar"
                )
                Spacer(Modifier.padding(dimensionResource(id = R.dimen.padding_small)))
                Text(
                    text = "VOLTAR",
                    textAlign = TextAlign.Left
                )
            }
        }
    )
}