package app.igormatos.botaprarodar.common.biding

import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.biding.utils.focusChangeListener
import app.igormatos.botaprarodar.common.biding.utils.textWatcherListener
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter(value = ["app:textCaptured", "app:errorMessage"])
fun TextInputLayout.setErrorUserCompleteName(userCompleteName: String, errorMessage: String) {
    val errorMessageId = this.resources.getIdentifier(errorMessage, "string", this.context.packageName)

    this.editText?.focusChangeListener(
        this,
        userCompleteName,
        errorMessageId
    )
    this.editText?.textWatcherListener(this, errorMessageId)
}