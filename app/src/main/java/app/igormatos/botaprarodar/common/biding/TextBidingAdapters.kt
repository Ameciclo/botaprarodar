package app.igormatos.botaprarodar.common.biding

import android.text.Editable
import android.text.TextWatcher
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.R
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("app:errorBicycleSerialNumber")
fun setErrorBicycleSerialNumber(view: TextInputLayout, bicycleSerialNumber: String) {
    view.editText?.focusChangeListener(view, bicycleSerialNumber, R.string.bicycle_serial_number_invalid)
    view.editText?.textWatcherListener(view, R.string.bicycle_serial_number_invalid)
}

@BindingAdapter("app:errorBicycleName")
fun setErrorBicycleName(view: TextInputLayout, bicycleName: String) {
    view.editText?.focusChangeListener(view, bicycleName, R.string.bicycle_name_invalid)
    view.editText?.textWatcherListener(view, R.string.bicycle_name_invalid)
}

@BindingAdapter("app:errorBicycleOrderNumber")
fun setErrorBicycleOrderNumber(view: TextInputLayout, bicycleOrderNumber: String) {
    view.editText?.focusChangeListener(view, bicycleOrderNumber, R.string.bicycle_invalid_order_number)
    view.editText?.textWatcherListener(view, R.string.bicycle_invalid_order_number)
}

private fun EditText.focusChangeListener(view: TextInputLayout, inputText: String, errorMessageId: Int) {
    onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            setError(inputText, view, errorMessageId)
        }
    }
}

private fun EditText.textWatcherListener(view : TextInputLayout, errorMessageId: Int) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            setError(s.toString(), view, errorMessageId)
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

private fun setError(data: String, view: TextInputLayout, errorMessageId: Int) {
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

