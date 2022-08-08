package app.igormatos.botaprarodar.common.biding

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import app.igormatos.botaprarodar.common.components.CustomDatePicker

@BindingAdapter("textValue")
fun CustomDatePicker.setTextValue(value: String?) {
    value.takeIf {
        it != getEditTextValue()
    }?.let {
        setEditTextValue(it)
    }
}

@InverseBindingAdapter(attribute = "textValue")
fun CustomDatePicker.getTextValue(): String {
    return getEditTextValue()
}