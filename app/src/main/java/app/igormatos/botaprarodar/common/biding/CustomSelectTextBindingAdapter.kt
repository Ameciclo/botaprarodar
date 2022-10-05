package app.igormatos.botaprarodar.common.biding

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.components.CustomEditTextWithButton
import app.igormatos.botaprarodar.common.components.CustomSelectText


@BindingAdapter("textValue")
fun CustomSelectText.setTextValue(value: String?) {
    value?.let {
        setEditTextValue(it)
        setupVisibility(it)
    }
}
@BindingAdapter("hintOpenField")
fun CustomSelectText.hintOpenField(value: String?) {
    value?.let {
        setHintOpenField(it)
    }
}


@BindingAdapter("textOpenFieldValue")
fun CustomSelectText.setTextOpenFieldValue(value: String?) {
    value.takeIf {
        it != getEditTextOpenFieldValue()
    }?.let {
        setEditTextOpenFieldValue(it)
    }
}

@InverseBindingAdapter(attribute = "textOpenFieldValue")
fun CustomSelectText.getTextOpenFieldValue(): String {
    return getEditTextOpenFieldValue()
}

@BindingAdapter("textOpenFieldValueAttrChanged")
fun CustomSelectText.setListener(listener: InverseBindingListener) {
    this.addEditTextOpenFieldListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            listener.onChange()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    })
}


