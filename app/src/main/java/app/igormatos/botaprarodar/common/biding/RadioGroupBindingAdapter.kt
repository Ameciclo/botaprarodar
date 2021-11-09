package app.igormatos.botaprarodar.common.biding

import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike.getRadioButtonIdByGiveRide
import app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike.getRadioButtonIdByReason
import app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike.getRadioButtonIdBySufferedViolence

@BindingAdapter("setRadioGroupReasonCheck")
fun RadioGroup.setRadioGroupReasonCheck(reasonId: String) {
    this.check(getRadioButtonIdByReason(reasonId))
}

@BindingAdapter("setRadioGroupReasonSufferedViolenceCheck")
fun RadioGroup.setRadioGroupSufferedViolenceCheck(reasonId: String) {
    this.check(getRadioButtonIdBySufferedViolence(reasonId))
}

@BindingAdapter("setRadioGroupGiveRideCheck")
fun RadioGroup.setRadioGroupGiveRideCheck(reasonId: String) {
    this.check(getRadioButtonIdByGiveRide(reasonId))
}

@BindingAdapter("setRadioGroupValue")
fun RadioGroup.setRadioGroupValue(buttonId: Int) {
    this.check(buttonId)
}

@BindingAdapter("radioGroupYesNoQuestionCheckedChanged")
fun RadioGroup.setRadioGroupYesNoQuestion(newValue: Boolean?) {
    newValue?.let { value ->
        val text = if (value) "sim" else "não"

        this.children.forEach { child ->
            val button = child as RadioButton
            if (button.text.toString().lowercase() == text) {
                this.check(button.id)
                return
            }
        }
    }
}

@InverseBindingAdapter(attribute = "radioGroupYesNoQuestionCheckedChanged", event = "radioGroupYesNoQuestionAttrChanged")
fun RadioGroup.getRadioGroupYesNoQuestion(): Boolean {
    val checkedId = this.checkedRadioButtonId
    val button: RadioButton = this.findViewById(checkedId)

    return when (button.text.toString().lowercase()) {
        "sim" -> true
        "não" -> false
        else -> false
    }
}

@BindingAdapter("radioGroupYesNoQuestionAttrChanged")
fun RadioGroup.setRadioGroupYesNoQuestionListener(listener: InverseBindingListener) {
    this.setOnCheckedChangeListener { _, _ ->
        listener.onChange()
    }
}