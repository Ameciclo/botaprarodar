package app.igormatos.botaprarodar.common.biding

import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.common.extensions.validateTextInFocusChange
import app.igormatos.botaprarodar.common.extensions.validateTextChanged
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter(value = ["app:textCaptured", "app:errorMessage"])
fun TextInputLayout.setErrorUserCompleteName(userCompleteName: String, errorMessage: String) {
    val errorMessageId = this.resources.getIdentifier(errorMessage, "string", this.context.packageName)

    this.editText?.validateTextInFocusChange(
        this,
        userCompleteName,
        errorMessageId
    )
    this.editText?.validateTextChanged(this, errorMessageId)
}