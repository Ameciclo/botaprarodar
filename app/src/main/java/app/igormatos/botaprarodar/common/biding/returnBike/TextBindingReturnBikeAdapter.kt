package app.igormatos.botaprarodar.common.biding.returnBike

import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.biding.utils.focusChangeListener
import app.igormatos.botaprarodar.common.biding.utils.textWatcherListener
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("app:errorDistrictName")
fun setErrorDistrictName(view: TextInputLayout, districtName: String) {
    view.editText?.focusChangeListener(view, districtName, R.string.invalid_district)
    view.editText?.textWatcherListener(view, R.string.invalid_district)
}