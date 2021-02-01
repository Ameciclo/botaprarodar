package app.igormatos.botaprarodar.common.biding

import android.widget.RadioGroup
import androidx.databinding.BindingAdapter

@BindingAdapter("app:setRadioGroupCheck")
fun setRadioGroupCheck(view: RadioGroup, radioButtonId: Int) {
    view.check(radioButtonId)
}