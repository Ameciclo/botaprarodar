package app.igormatos.botaprarodar.common.biding

import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.biding.utils.focusChangeListener
import app.igormatos.botaprarodar.common.biding.utils.textWatcherListener
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("app:errorUserCompleteName")
fun setErrorUserCompleteName(view: TextInputLayout, userCompleteName: String) {
    view.editText?.focusChangeListener(
        view,
        userCompleteName,
        R.string.add_user_invalid_name
    )
    view.editText?.textWatcherListener(view, R.string.add_user_invalid_name)
}

@BindingAdapter("app:errorUserAddress")
fun setErrorUserAddress(view: TextInputLayout, userCompleteName: String) {
    view.editText?.focusChangeListener(
        view,
        userCompleteName,
        R.string.add_user_invalid_address
    )
    view.editText?.textWatcherListener(view, R.string.add_user_invalid_address)
}

@BindingAdapter("app:errorUserCpf")
fun setErrorUserCpf(view: TextInputLayout, userCompleteName: String) {
    view.editText?.focusChangeListener(
        view,
        userCompleteName,
        R.string.add_user_invalid_cpf
    )
    view.editText?.textWatcherListener(view, R.string.add_user_invalid_cpf)
}