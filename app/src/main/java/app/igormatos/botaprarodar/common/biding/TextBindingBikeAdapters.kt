package app.igormatos.botaprarodar.common.biding

import android.widget.TextView
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

@BindingAdapter("bikeNameWithLabel")
fun TextView.setBikeNameWithLabel(userName: String) {
    text = userName
}

@BindingAdapter("bikeOrderWithLabel")
fun TextView.setBikeOrderWithLabel(order: String) {
    text = context.getString(R.string.bike_order_with_label, order)
}

@BindingAdapter("bikeSeriesWithLabel")
fun TextView.setBikeSeriesWithLabel(series: String) {
    text = context.getString(R.string.bike_series_with_label, series)
}
