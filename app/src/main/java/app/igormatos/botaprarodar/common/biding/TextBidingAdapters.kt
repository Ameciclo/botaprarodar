package app.igormatos.botaprarodar.common.biding

import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.R
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("app:errorBicycleSerialNumber")
fun setErrorBicycleSerialNumber(view: TextInputLayout, bicycleSerialNumber: String?) {
    if (!bicycleSerialNumber.isNullOrBlank()) {
        view.error = null
    } else {
        view.error = view.context.getString(R.string.bicycle_serial_number_invalid)
    }
}

@BindingAdapter("app:errorBicycleName")
fun setErrorBicycleName(view: TextInputLayout, bicycleName: String?) {
    if (!bicycleName.isNullOrBlank()) {
        view.error = null
    } else {
        view.error = view.context.getString(R.string.bicycle_name_invalid)
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

