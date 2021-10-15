package app.igormatos.botaprarodar.common.biding

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MediatorLiveData
import app.igormatos.botaprarodar.common.extensions.focusChangedErrorListener
import app.igormatos.botaprarodar.common.extensions.textChangedErrorListener
import app.igormatos.botaprarodar.common.extensions.validateTextChanged
import app.igormatos.botaprarodar.common.extensions.validateTextInFocusChange
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

@BindingAdapter("app:errorUserDocNumber")
fun TextInputLayout.setErrorUserDocNumber(
                                docNumberErrorValidationMap: MediatorLiveData<MutableMap<Int, Boolean>>
) {
    this.editText?.focusChangedErrorListener(
        docNumberErrorValidationMap,
        this)
    this.editText?.textChangedErrorListener(docNumberErrorValidationMap, this)
}