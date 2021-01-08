package app.igormatos.botaprarodar.presentation.authentication.viewmodel

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @BindingAdapter("app:progressEnabled")
    @JvmStatic
    fun isProgressEnabled(view: ProgressBar, enabled: Boolean) {
        return if (enabled) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }
}
