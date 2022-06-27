package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.igormatos.botaprarodar.R

@Composable
fun ActionCard(
    title: String,
    icon: Painter,
    isEnable: Boolean = true,
    onClickHandler: () -> Unit = {}
) {
    Card(
        elevation = 3.dp,
        modifier = Modifier
            .width(170.dp)
            .clickable(enabled = isEnable) { onClickHandler() }
    ) {
        Column(
            Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .alpha(if (!isEnable) 0.6f else 1f)
        ) {
            Image(
                painter = icon,
                contentDescription = "",
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(40.dp)
            )
            Text(
                text = title.uppercase(),
                fontSize = 18.sp,
                color = colorResource(id = R.color.text_gray),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActionCardPreview() {
    ActionCard(title = "Devolver bicicleta", icon = painterResource(id = R.drawable.ic_return_bike))
}