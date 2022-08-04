package app.igormatos.botaprarodar.common.biding

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.MediatorLiveData
import app.igormatos.botaprarodar.common.components.CustomDatePicker
import app.igormatos.botaprarodar.common.components.CustomEditText

@BindingAdapter(value = ["textCaptured", "errorMessage"])
fun CustomDatePicker.setErrorUserCompleteName(userCompleteName: String?, errorMessage: String) {
    this.validateText(userCompleteName, errorMessage)
}

@BindingAdapter("errorUserDocNumber")
fun CustomDatePicker.setErrorUserCompleteName(docNumberErrorValidationMap: MediatorLiveData<MutableMap<Int, Boolean>>) {
    this.validateDocument(docNumberErrorValidationMap)
}

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