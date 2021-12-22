package app.igormatos.botaprarodar.common.biding

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import app.igormatos.botaprarodar.common.components.CustomEditTextWithButton

@BindingAdapter("textValueAttrChanged")
fun CustomEditTextWithButton.setListener(listener: InverseBindingListener) {
    this.addEditTextListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            listener.onChange()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    })
}

@BindingAdapter("textValue")
fun CustomEditTextWithButton.setTextValue(value: String?) {
    value.takeIf {
        it != getEditTextValue()
    }?.let {
        setEditTextValue(it)
    }
}

@BindingAdapter("setRadioGroupYesNoQuestionCheckedChanged")
fun CustomEditTextWithButton.setupRadioGroupYesNoQuestion(newValue: Boolean?) {
    setRadioGroupYesNoQuestion(getRadioGroup(), newValue)
}

@InverseBindingAdapter(attribute = "setRadioGroupYesNoQuestionCheckedChanged", event = "setRadioGroupYesNoQuestionAttrChanged")
fun CustomEditTextWithButton.getRadioGroupYesNoQuestion(): Boolean {
    return getRadioGroupYesNoQuestion(getRadioGroup())
}

@BindingAdapter("setRadioGroupYesNoQuestionAttrChanged")
fun CustomEditTextWithButton.setRadioGroupYesNoQuestionListener(listener: InverseBindingListener) {
    getRadioGroup().setOnCheckedChangeListener { _, buttonId ->
        listener.onChange()
        setupVisibility(buttonId)
    }
}
@InverseBindingAdapter(attribute = "textValue")
fun CustomEditTextWithButton.getTextValue(): String {
    return getEditTextValue()
}