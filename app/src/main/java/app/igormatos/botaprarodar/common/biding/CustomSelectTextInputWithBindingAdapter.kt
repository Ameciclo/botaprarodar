package app.igormatos.botaprarodar.common.biding

import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.common.components.CustomSelectText
import app.igormatos.botaprarodar.common.components.CustomSelectTextWithTextInput


@BindingAdapter("textValue")
fun CustomSelectTextWithTextInput.setTextValue(value: String?) {
    value?.let {
        setEditTextValue(it)
    }
}
