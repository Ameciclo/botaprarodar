package app.igormatos.botaprarodar.common.biding.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

fun EditText.focusChangeListener(view: TextInputLayout, inputText: String, errorMessageId: Int) {
    onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            setError(inputText, view, errorMessageId)
        }
    }
}

fun EditText.textWatcherListener(view: TextInputLayout, errorMessageId: Int) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            setError(s.toString(), view, errorMessageId)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

fun setError(data: String, view: TextInputLayout, errorMessageId: Int) {
    when {
        data.isNotBlank() -> {
            view.isErrorEnabled = false
            view.error = null
        }
        else -> {
            view.isErrorEnabled = true
            view.error = view.context.getString(errorMessageId)
        }
    }
}