package app.igormatos.botaprarodar.common.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.biding.setErrorUserCompleteName
import app.igormatos.botaprarodar.common.extensions.validateTextChanged
import app.igormatos.botaprarodar.databinding.CustomEditTextBinding

class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = CustomEditTextBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        orientation = VERTICAL
        //binding.editText.setText("TESTE")

        attrs?.let {
            val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomEditText, 0,0)
            binding.label.text = typedArray.getString(R.styleable.CustomEditText_text_label)
            binding.editText.hint = typedArray.getString(R.styleable.CustomEditText_text_hint)
        }
    }

    fun setupText(userCompleteName: String, errorMessage: String){
        binding.apply {
            textLayout.setErrorUserCompleteName(
                userCompleteName,
                errorMessage
            )
        }
    }

}