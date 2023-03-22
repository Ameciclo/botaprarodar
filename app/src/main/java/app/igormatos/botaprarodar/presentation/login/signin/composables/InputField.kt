package app.igormatos.botaprarodar.presentation.login.signin.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import app.igormatos.botaprarodar.R

@Composable
fun InputField(
    state: String,
    label: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null,
    imeAction: ImeAction,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    errorMessage: String? = null,
    onValueChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = state,
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null,
            label = label,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                errorBorderColor = Color.Red,
                focusedBorderColor = colorResource(id = R.color.green_teal),
                cursorColor = colorResource(id = R.color.green_teal),
                focusedLabelColor = colorResource(id = R.color.green_teal)
            ),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
            ),
            onValueChange = onValueChange,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() },
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true
        )

        errorMessage?.let {
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = it,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
            )
        }
    }
}