package app.igormatos.botaprarodar.common.biding

import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.presentation.userForm.getRadioButtonId

@BindingAdapter("app:setRadioGroupCheck")
fun setRadioGroupCheck(view: RadioGroup, genderId: Int) {
    view.check(getRadioButtonId(genderId))
}