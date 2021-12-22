package app.igormatos.botaprarodar.common.biding

import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.R

@BindingAdapter("title")
fun setToolbarTitle(view: Toolbar, isEditMode: Boolean) {
    if (isEditMode){
        view.title = view.context.getString(R.string.edit_bicycle_title)
    } else {
        view.title = view.context.getString(R.string.add_bicycle_title)
    }
}

@BindingAdapter("setTitle")
fun setAddUserToolbarTitle(view: Toolbar, isEditMode: Boolean) {
    if (isEditMode){
        view.title = view.context.getString(R.string.edit_user_toolbar_title)
    } else {
        view.title = view.context.getString(R.string.add_user_toolbar_title)
    }
}