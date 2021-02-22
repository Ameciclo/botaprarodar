package app.igormatos.botaprarodar.common.biding

import android.widget.Button
import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.R

@BindingAdapter("app:setBikeFormButtonText")
fun setBikeFormButtonText(view: Button, isEditModeAvailable: Boolean) {
    if (isEditModeAvailable) {
        view.text = view.context.getString(R.string.bicycle_update_button)
    } else {
        view.text = view.context.getString(R.string.btn_register_bicycle)
    }
}

@BindingAdapter("app:setUserFormButtonText")
fun setUserFormButtonText(view: Button, isEditModeAvailable: Boolean) {
    if (isEditModeAvailable) {
        view.text = view.context.getString(R.string.btn_edit_user)
    } else {
        view.text = view.context.getString(R.string.btn_add_user)
    }
}