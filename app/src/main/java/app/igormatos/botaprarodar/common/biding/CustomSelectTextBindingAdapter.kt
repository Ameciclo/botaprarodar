package app.igormatos.botaprarodar.common.biding

import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.common.components.CustomSelectText


@BindingAdapter("textValue")
fun CustomSelectText.setTextValue(value: String?) {
    value?.let {
        setEditTextValue(it)
        setupVisibility(it)
    }
}
