package app.igormatos.botaprarodar.common.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.CustomSelectTextBinding

class CustomSelectText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = CustomSelectTextBinding.inflate(
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
            with(binding) {
                label.text = typedArray.getString(R.styleable.CustomSelectText_android_label)
                selectorEditText.hint =
                    typedArray.getString(R.styleable.CustomSelectText_android_hint)
                questionLabel.text =
                    typedArray.getString(R.styleable.CustomSelectText_questionLabelSelectText)
                isNeedQuestion = typedArray.getBoolean(
                    R.styleable.CustomSelectText_isNeedQuestionSelectText,
                    false
                )
                editText.hint = typedArray.getString(R.styleable.CustomSelectText_android_hint)
            }
        }
    }

    fun setupClick(clickAction: () -> Unit) {
        binding.selectorEditText.setOnClickListener {
            clickAction.invoke()
        }
    }

    fun setEditTextValue(value: String) {
        binding.selectorEditText.setText(value)
    }

    fun setupVisibility(value: String) {
        if (value == "Outro" && isNeedQuestion) {
            binding.textLayout.visibility = VISIBLE
            binding.questionLabel.visibility = VISIBLE
        } else {
            binding.textLayout.visibility = GONE
            binding.questionLabel.visibility = GONE
        }
    }

}
