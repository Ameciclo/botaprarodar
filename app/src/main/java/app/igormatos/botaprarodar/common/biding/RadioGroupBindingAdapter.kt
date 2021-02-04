package app.igormatos.botaprarodar.common.biding

import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.R
import kotlinx.android.synthetic.main.activity_add_user.view.*

@BindingAdapter("app:setRadioGroupCheck")
fun setRadioGroupCheck(view: RadioGroup, genderId: Int) {
    view.check(getRadioButtonId(genderId))
}

private fun getRadioButtonId(genderId: Int) =
    when (genderId) {
        0 -> R.id.rbGenderMale
        1 -> R.id.rbGenderFemale
        2 -> R.id.rbGenderOther
        else -> R.id.rbGenderNoAnswer
    }