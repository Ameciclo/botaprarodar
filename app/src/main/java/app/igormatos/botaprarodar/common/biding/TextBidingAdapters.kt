package app.igormatos.botaprarodar.common.biding

import android.view.View.OnFocusChangeListener
import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.R
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("app:errorBicycleSerialNumber")
fun setErrorBicycleSerialNumber(view: TextInputLayout, bicycleSerialNumber: String) {
    view.editText?.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
        if (!hasFocus) {
            setError( bicycleSerialNumber, view, R.string.bicycle_serial_number_invalid)
        }
    }
}

@BindingAdapter("app:errorBicycleName")
fun setErrorBicycleName(view: TextInputLayout, bicycleName: String) {
    view.editText?.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
        if (!hasFocus) {
            setError(bicycleName, view,R.string.bicycle_name_invalid)
        }
    }
}

@BindingAdapter("app:errorBicycleOrderNumber")
fun setErrorBicycleOrderNumber(view: TextInputLayout, bicycleOrderNumber: String?) {
    if (!bicycleOrderNumber.isNullOrBlank()) {
        view.error = null
    } else {
        view.error = view.context.getString(R.string.bicycle_serial_number_invalid)
    }
}

private fun setError(data: String, view: TextInputLayout, errorMessageId:Int ) {
    when {
//        bicycleSerialNumber != null && bicycleSerialNumber.isNotEmpty() -> {
//            view.error = null
//        }
        data.isNotBlank() -> {
            view.error = ""
        }
        else -> {
            view.error =  view.context.getString(errorMessageId)
        }
    }
}

