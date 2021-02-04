package app.igormatos.botaprarodar.common.biding

import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.R

@BindingAdapter("app:title")
fun setToolbarTitle(view: androidx.appcompat.widget.Toolbar, isEditMode: Boolean) {
    if (isEditMode){
        view.title = view.context.getString(R.string.edit_bicycle_title)
    } else {
        view.title = view.context.getString(R.string.add_bicycle_title)
    }
}