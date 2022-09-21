package app.igormatos.botaprarodar.common.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.CustomSelectTextWithTextInputBinding

class CustomSelectTextWithTextInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = CustomSelectTextWithTextInputBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private var isNeedQuestion = false

    init {
        orientation = VERTICAL
        attrs?.let { attributes ->
            val typedArray = context.theme.obtainStyledAttributes(
                attributes,
                R.styleable.CustomSelectText,
                0,
                0
            )
            with(binding){
                label.text = typedArray.getString(R.styleable.CustomEditText_android_label)
                selectorEditText.hint = typedArray.getString(R.styleable.CustomEditText_android_hint)
                questionLabel.text = typedArray.getString(R.styleable.CustomEditTextWithButton_questionLabel)
                isNeedQuestion = typedArray.getBoolean(R.styleable.CustomEditTextWithButton_isNeedQuestion, false)
                editText.inputType = typedArray.getInt(
                    R.styleable.CustomEditTextWithButton_android_inputType,
                    EditorInfo.TYPE_NULL
                )
            }
        }
    }

    fun setupClick(clickAction: () -> Unit){
        binding.selectorEditText.setOnClickListener {
            clickAction.invoke()
        }
    }

    fun setEditTextValue(value: String) {
        binding.selectorEditText.setText(value)
    }

}
