package app.igormatos.botaprarodar.common.components

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
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
        attrs?.let { attributes ->
            val typedArray = context.theme.obtainStyledAttributes(
                attributes,
                R.styleable.CustomEditText,
                0,
                0
            )
            with(binding){
                label.text = typedArray.getString(R.styleable.CustomEditText_text_label)
                editText.hint = typedArray.getString(R.styleable.CustomEditText_text_hint)
                editText.inputType = typedArray.getInt(
                    R.styleable.CustomEditText_android_inputType,
                    EditorInfo.TYPE_NULL
                )
            }
        }
    }

    fun getEditTextValue() = binding.editText.text.toString()
    fun setEditTextValue(value: String) {
        binding.editText.setText(value)
    }

    fun addEditTextListener(textWatcher: TextWatcher){
        binding.editText.addTextChangedListener(textWatcher)
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