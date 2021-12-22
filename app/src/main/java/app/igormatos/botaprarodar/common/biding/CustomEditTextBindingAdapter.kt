package app.igormatos.botaprarodar.common.biding

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.MediatorLiveData
import app.igormatos.botaprarodar.common.components.CustomEditText

@BindingAdapter(value = ["textCaptured", "errorMessage"])
fun CustomEditText.setErrorUserCompleteName(userCompleteName: String?, errorMessage: String) {
    this.validateText(userCompleteName, errorMessage)
}

@BindingAdapter("errorUserDocNumber")
fun CustomEditText.setErrorUserCompleteName(docNumberErrorValidationMap: MediatorLiveData<MutableMap<Int, Boolean>>) {
    this.validateDocument(docNumberErrorValidationMap)
}

@BindingAdapter("textValueAttrChanged")
fun CustomEditText.setListener(listener: InverseBindingListener) {
    this.addEditTextListener(object : TextWatcher {
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