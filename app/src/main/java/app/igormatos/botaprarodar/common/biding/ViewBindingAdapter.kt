package app.igormatos.botaprarodar.common.biding

import android.view.View
import androidx.databinding.BindingAdapter
import com.brunotmgomes.ui.extensions.gone
import com.brunotmgomes.ui.extensions.visible

@BindingAdapter("setVisibility")
fun setVisibilityView(view: View, show: Boolean) {
    if (show) {
        view.visible()
    } else {
        view.gone()
    }
}