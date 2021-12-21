package app.igormatos.botaprarodar.presentation.components.badges

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.components.ui.theme.Typography

@Composable
fun BikeBadge(text: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .size(width = 104.dp, height = 40.dp)
            .padding(
                top = dimensionResource(id = R.dimen.padding_medium),
                start = dimensionResource(id = R.dimen.padding_medium),
            )
            .background(shape = RoundedCornerShape(20),
                color = backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            fontStyle = Typography.subtitle1.fontStyle,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}