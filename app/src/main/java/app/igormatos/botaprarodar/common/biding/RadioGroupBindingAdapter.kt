package app.igormatos.botaprarodar.common.biding

import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.children
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike.getRadioButtonIdByGiveRide
import app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike.getRadioButtonIdBySufferedViolence

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
fun setRadioGroupYesNoQuestion(group: RadioGroup, newValue: Boolean?) {
    newValue?.let { value ->
        val text = if (value) "sim" else "não"

        group.children.forEach { child ->
            val button = child as RadioButton
            if (button.text.toString().lowercase() == text) {
                group.check(button.id)
                return
            }
        }
    }
}

@InverseBindingAdapter(attribute = "radioGroupYesNoQuestionCheckedChanged",  event = "radioGroupYesNoQuestionAttrChanged")
fun getRadioGroupYesNoQuestion(group: RadioGroup): Boolean {
    val checkedId = group.checkedRadioButtonId
    val button: RadioButton = group.findViewById(checkedId)

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