package app.igormatos.botaprarodar.common.biding

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import app.igormatos.botaprarodar.common.components.CustomEditText

@BindingAdapter(value = ["app:textCaptured", "app:errorMessage"])
fun CustomEditText.setText(userCompleteName: String, errorMessage: String){
    this.setupText(userCompleteName, errorMessage)
}


@BindingAdapter("textValueAttrChanged")
fun CustomEditText.setListener(listener: InverseBindingListener) {
    addEditTextListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            listener.onChange()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    })
}

@BindingAdapter("textValue")
fun CustomEditText.setTextValue(value: String?) {
    value.takeIf {
        it != getEditTextValue()
    }?.let {
        setEditTextValue(it)
    }
}

@InverseBindingAdapter(attribute = "textValue")
fun CustomEditText.getTextValue(): String {
    return getEditTextValue()
}