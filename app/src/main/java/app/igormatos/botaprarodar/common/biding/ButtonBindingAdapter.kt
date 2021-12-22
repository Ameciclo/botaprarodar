package app.igormatos.botaprarodar.common.biding

import android.widget.Button
import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.R

@BindingAdapter("setBikeFormButtonText")
fun Button.setBikeFormButtonText(isEditModeAvailable: Boolean) {
    if (isEditModeAvailable) {
        this.text = this.context.getString(R.string.bicycle_update_button)
    } else {
        this.text = this.context.getString(R.string.btn_register_bicycle)
    }
}

@BindingAdapter("setUserFormButtonText")
fun Button.setUserFormButtonText(isEditModeAvailable: Boolean) {
    if (isEditModeAvailable) {
        this.text = this.context.getString(R.string.btn_edit_user)
    } else {
        this.text = this.context.getString(R.string.btn_add_user)
    }
}