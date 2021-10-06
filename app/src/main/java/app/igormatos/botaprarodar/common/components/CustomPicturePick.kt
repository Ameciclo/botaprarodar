package app.igormatos.botaprarodar.common.components

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.biding.setErrorUserCompleteName
import app.igormatos.botaprarodar.common.extensions.validateTextChanged
import app.igormatos.botaprarodar.databinding.CustomEditTextBinding
import app.igormatos.botaprarodar.databinding.CustomPicturePickBinding

class CustomPicturePick @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = CustomPicturePickBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        attrs?.let { attributes ->
            val typedArray = context.theme.obtainStyledAttributes(
                attributes,
                R.styleable.CustomEditText,
                0,
                0
            )
            with(binding) {
                label.text = typedArray.getString(R.styleable.CustomEditText_text_label)
            }

        }
    }

}