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

@BindingAdapter("app:setRadioGroupReasonCheck")
fun setRadioGroupReasonCheck(view: RadioGroup, reasonId: String) {
    view.check(getRadioButtonIdByReason(reasonId))
}

@BindingAdapter("app:setRadioGroupReasonSufferedViolenceCheck")
fun setRadioGroupSufferedViolenceCheck(view: RadioGroup, reasonId: String) {
    view.check(getRadioButtonIdBySufferedViolence(reasonId))
}

@BindingAdapter("app:setRadioGroupGiveRideCheck")
fun setRadioGroupGiveRideCheck(view: RadioGroup, reasonId: String) {
    view.check(getRadioButtonIdByGiveRide(reasonId))
}

@BindingAdapter("app:setRadioGroupValue")
fun setRadioGroupValue(view: RadioGroup, buttonId: Int) {
    view.check(buttonId)
}

@BindingAdapter("radioGroupYesNoQuestionCheckedChanged")
fun setRadioGroupYesNoQuestion(group: RadioGroup, newValue: Boolean?) {
    newValue?.let { value ->
        val text = if (value) "sim" else "não"

        group.children.forEach { child ->
            val button = child as RadioButton
            if (button.text.toString().toLowerCase() == text) {
                group.check(button.id)
                return
            }
        }
    }
}

@InverseBindingAdapter(attribute = "radioGroupYesNoQuestionCheckedChanged", event = "radioGroupYesNoQuestionAttrChanged")
fun getRadioGroupYesNoQuestion(group: RadioGroup): Boolean {
    val checkedId = group.checkedRadioButtonId
    val button: RadioButton = group.findViewById(checkedId)

    return when (button.text.toString().toLowerCase()) {
        "sim" -> true
        "não" -> false
        else -> false
    }
}

@BindingAdapter("radioGroupYesNoQuestionAttrChanged")
fun setRadioGroupYesNoQuestionListener(view: RadioGroup, listener: InverseBindingListener) {
    view.setOnCheckedChangeListener { _, _ ->
        listener.onChange()
    }
}