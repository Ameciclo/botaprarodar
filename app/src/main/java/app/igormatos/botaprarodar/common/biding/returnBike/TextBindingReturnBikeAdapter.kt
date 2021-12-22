package app.igormatos.botaprarodar.common.biding.returnBike

import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.extensions.validateTextInFocusChange
import app.igormatos.botaprarodar.common.extensions.validateTextChanged
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorDistrictName")
fun setErrorDistrictName(view: TextInputLayout, districtName: String) {
    view.editText?.validateTextInFocusChange(view, districtName, R.string.invalid_district)
    view.editText?.validateTextChanged(view, R.string.invalid_district)
}