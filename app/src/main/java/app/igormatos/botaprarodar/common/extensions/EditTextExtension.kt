package app.igormatos.botaprarodar.common.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.lifecycle.MediatorLiveData
import app.igormatos.botaprarodar.common.biding.utils.validateField
import app.igormatos.botaprarodar.common.biding.utils.validateText
import com.google.android.material.textfield.TextInputLayout

fun EditText.validateTextInFocusChange(
    view: TextInputLayout,
    inputText: String?,
    errorMessageId: Int
) {
    onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            validateText(inputText, view, errorMessageId)
        }
    }
}

fun EditText.validateTextChanged(view: TextInputLayout, errorMessageId: Int) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            validateText(editable.toString(), view, errorMessageId)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun EditText.focusChangedErrorListener(
    validationErrorMap: MediatorLiveData<MutableMap<Int, Boolean>>,
    view: TextInputLayout,
) {
    onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            validateField(validationErrorMap, view)
        }
    }
}

fun EditText.textChangedErrorListener(
    validationErrorMap: MediatorLiveData<MutableMap<Int, Boolean>>,
    view: TextInputLayout,
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            validateField(validationErrorMap, view)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

