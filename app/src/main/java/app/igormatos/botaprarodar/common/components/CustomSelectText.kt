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
                editText.hint = typedArray.getString(R.styleable.CustomEditText_android_hint)
            }
        }
    }

    fun setupClick(clickAction: () -> Unit){
        binding.editText.setOnClickListener {
            clickAction.invoke()
        }
    }

    fun setEditTextValue(value: String) {
        binding.editText.setText(value)
    }

}