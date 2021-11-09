package app.igormatos.botaprarodar.presentation.authentication.viewmodel

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.R
import com.airbnb.lottie.LottieAnimationView
import com.brunotmgomes.ui.extensions.isValidPassword


object BindingAdapters {
    @BindingAdapter("animationVisibility")
    @JvmStatic
    fun isViewVisible(view: LottieAnimationView, visible: Boolean) {
        if (visible) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }

    @BindingAdapter(value = ["onPasswordValidate", "onSubmitForm"], requireAll = false)
    @JvmStatic
    fun validatePassword(view: EditText, password: String?, sendFormView: View?) {
        view.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && password.isValidPassword()) {
                sendFormView?.callOnClick()
                view.error = null
            } else {
                view.error = view.resources.getString(R.string.password_length_error)
            }
            true
        }
    }
}
