package app.igormatos.botaprarodar.common.components

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.biding.utils.validateText
import app.igormatos.botaprarodar.common.utils.EditTextFormatMask
import app.igormatos.botaprarodar.databinding.CustomEditTextWithButtonBinding

class CustomEditTextWithButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = CustomEditTextWithButtonBinding.inflate(
        LayoutInflater.from(context), this, true
    )
    private var isNeedQuestion = false

    init {
        orientation = VERTICAL
        attrs?.let { attributes ->
            val typedArray = context.theme.obtainStyledAttributes(
                attributes,
                R.styleable.CustomEditTextWithButton,
                0,
                0
            )
            with(binding){
                label.text = typedArray.getString(R.styleable.CustomEditTextWithButton_android_label)
                questionLabel.text = typedArray.getString(R.styleable.CustomEditTextWithButton_questionLabel)
                editText.hint = typedArray.getString(R.styleable.CustomEditTextWithButton_android_hint)
                isNeedQuestion = typedArray.getBoolean(R.styleable.CustomEditTextWithButton_isNeedQuestion, false)
                editText.inputType = typedArray.getInt(
                    R.styleable.CustomEditTextWithButton_android_inputType,
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

    fun addMask(format: String){
        binding.editText.addTextChangedListener(
            EditTextFormatMask.textMask(binding.editText, format)
        )
    }

    fun validate(errorMessageId: Int){
        binding.editText.doAfterTextChanged {
            validateText(it.toString(), binding.textLayout, errorMessageId)
        }
    }

    fun getRadioGroup () = binding.radioGrup

    fun setupVisibility(buttonId: Int) {

        if(buttonId == binding.buttonYes.id && isNeedQuestion){
            binding.textLayout.visibility = VISIBLE
            binding.questionLabel.visibility = VISIBLE
        } else {
            binding.textLayout.visibility = GONE
            binding.questionLabel.visibility = GONE
        }
    }
}