package app.igormatos.botaprarodar.common.biding.utils

import androidx.lifecycle.MediatorLiveData
import com.google.android.material.textfield.TextInputLayout

fun validateText(data: String, view: TextInputLayout, errorMessageId: Int) {
    if (data.isNotBlank()) {
        clearError(view)
    } else {
        displayError(view, errorMessageId)
    }
}

fun validateField(
    validationErrorMap: MediatorLiveData<MutableMap<Int, Boolean>>,
    view: TextInputLayout
) {
    validationErrorMap.value?.forEach {
        if (it.value) {
            displayError(view, it.key)
            return
        }
    }
    clearError(view)
}

private fun displayError(
    view: TextInputLayout,
    errorMessageId: Int
) {
    view.isErrorEnabled = true
    view.error = view.context.getString(errorMessageId)
}

private fun clearError(view: TextInputLayout) {
    view.isErrorEnabled = false
    view.error = null
}