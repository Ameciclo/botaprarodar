package app.igormatos.botaprarodar.common.biding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MediatorLiveData
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.extensions.focusChangedErrorListener
import app.igormatos.botaprarodar.common.extensions.textChangedErrorListener
import app.igormatos.botaprarodar.common.extensions.validateTextChanged
import app.igormatos.botaprarodar.common.extensions.validateTextInFocusChange
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("app:errorBicycleSerialNumber")
fun setErrorBicycleSerialNumber(view: TextInputLayout,
                                serialNumberErrorValidationMap: MediatorLiveData<MutableMap<Int, Boolean>>) {
    view.editText?.focusChangedErrorListener(
        serialNumberErrorValidationMap,
        view)
    view.editText?.textChangedErrorListener(serialNumberErrorValidationMap, view)
}

@BindingAdapter("app:errorBicycleName")
fun setErrorBicycleName(view: TextInputLayout, bicycleName: String) {
    view.editText?.validateTextInFocusChange(view, bicycleName, R.string.bicycle_name_invalid)
    view.editText?.validateTextChanged(view, R.string.bicycle_name_invalid)
}

@BindingAdapter("app:errorBicycleOrderNumber")
fun setErrorBicycleOrderNumber(view: TextInputLayout, bicycleOrderNumber: String) {
    view.editText?.validateTextInFocusChange(
        view,
        bicycleOrderNumber,
        R.string.bicycle_invalid_order_number
    )
    view.editText?.validateTextChanged(view, R.string.bicycle_invalid_order_number)
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
