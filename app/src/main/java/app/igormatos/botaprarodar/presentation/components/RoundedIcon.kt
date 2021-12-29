package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import app.igormatos.botaprarodar.presentation.components.ui.theme.ColorPalet


data class IconStyle(
    var icon: Painter,
    var iconColor: Color = ColorPalet.AuxiliarGray,
    var lineColor: Color = ColorPalet.AuxiliarGray,
    var backgroundColor: Color = Color.White
)

@Composable
fun ItemLineSetStepperComponent(
    iconStyle: IconStyle
) {
    ItemStepperComponent(
        iconStyle = iconStyle
    )
    Divider(modifier = Modifier.width(40.dp), color = iconStyle.lineColor)
}

@Composable
fun ItemStepperComponent(
    iconStyle: IconStyle
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .border(
                border = BorderStroke(1.dp, color = iconStyle.lineColor),
                shape = CircleShape
            )
            .background(iconStyle.backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .size(14.dp)
                .background(iconStyle.backgroundColor),
            painter = iconStyle.icon,
            tint = iconStyle.iconColor,
            contentDescription = "Icon Description",
        )
    }
}