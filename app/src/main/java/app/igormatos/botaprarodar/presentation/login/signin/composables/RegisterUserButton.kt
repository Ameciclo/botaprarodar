package app.igormatos.botaprarodar.presentation.login.signin.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import app.igormatos.botaprarodar.R

@Composable
fun RegisterUserButton(onClick: () -> Unit,) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.wrapContentSize(),
        contentPadding = PaddingValues(8.dp),
    ) {
        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = colorResource(R.color.green_teal),
                        fontWeight = FontWeight.Normal,
                    )
                ) {
                    append(stringResource(id = R.string.no_account))
                }
                append(" ")
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(R.color.green_teal),
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append(stringResource(id = R.string.register))
                }
            },
        )
    }
}