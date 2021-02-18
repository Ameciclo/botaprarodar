package app.igormatos.botaprarodar.common.biding

import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.biding.utils.focusChangeListener
import app.igormatos.botaprarodar.common.biding.utils.textWatcherListener
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("app:errorBicycleSerialNumber")
fun setErrorBicycleSerialNumber(view: TextInputLayout, bicycleSerialNumber: String) {
    view.editText?.focusChangeListener(
        view,
        bicycleSerialNumber,
        R.string.bicycle_serial_number_invalid
    )
    view.editText?.textWatcherListener(view, R.string.bicycle_serial_number_invalid)
}

@BindingAdapter("app:errorBicycleName")
fun setErrorBicycleName(view: TextInputLayout, bicycleName: String) {
    view.editText?.focusChangeListener(view, bicycleName, R.string.bicycle_name_invalid)
    view.editText?.textWatcherListener(view, R.string.bicycle_name_invalid)
}

@BindingAdapter("app:errorBicycleOrderNumber")
fun setErrorBicycleOrderNumber(view: TextInputLayout, bicycleOrderNumber: String) {
    view.editText?.focusChangeListener(
        view,
        bicycleOrderNumber,
        R.string.bicycle_invalid_order_number
    )
    view.editText?.textWatcherListener(view, R.string.bicycle_invalid_order_number)
}