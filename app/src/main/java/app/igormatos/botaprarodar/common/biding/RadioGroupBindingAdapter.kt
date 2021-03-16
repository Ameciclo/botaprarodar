package app.igormatos.botaprarodar.common.biding

import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike.getRadioButtonIdByGiveRide
import app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike.getRadioButtonIdByReason
import app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike.getRadioButtonIdBySufferedViolence
import app.igormatos.botaprarodar.presentation.userForm.getRadioButtonId

@BindingAdapter("app:setRadioGroupCheck")
fun setRadioGroupCheck(view: RadioGroup, genderId: Int) {
    view.check(getRadioButtonId(genderId))
}

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