package app.igormatos.botaprarodar.presentation.authentication.viewmodel

import android.view.View
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView

object BindingAdapters {
    @BindingAdapter("app:animationVisibility")
    @JvmStatic
    fun isViewVisible(view: LottieAnimationView, visible: Boolean) {
        return if (visible) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }
}
