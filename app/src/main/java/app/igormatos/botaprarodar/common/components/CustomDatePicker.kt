package app.igormatos.botaprarodar.common.components

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MediatorLiveData
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.biding.setErrorUserCompleteName
import app.igormatos.botaprarodar.common.biding.setErrorUserDocNumber
import app.igormatos.botaprarodar.common.utils.EditTextFormatMask
import app.igormatos.botaprarodar.databinding.CustomDatePickerBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.custom_edit_text.view.*
import org.jetbrains.anko.activityManager
import org.jetbrains.anko.appcompat.v7.Appcompat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class CustomDatePicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = CustomDatePickerBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        orientation = VERTICAL
        attrs?.let { attributes ->
            val typedArray = context.theme.obtainStyledAttributes(
                attributes,
                R.styleable.CustomDatePicker,
                0,
                0
            )
            with(binding){
                label.text = typedArray.getString(R.styleable.CustomDatePicker_android_label)
                editText.hint = typedArray.getString(R.styleable.CustomDatePicker_android_hint)
            }
        }
    }

    fun getEditTextValue() = binding.editText.text.toString()
    fun setEditTextValue(value: String) {
        binding.editText.setText(value)
    }

    fun clearEditTextValue() {
        binding.editText.text?.clear()
    }

    fun validateText(userCompleteName: String?, errorMessage: String) {
        binding.apply {
            textLayout.setErrorUserCompleteName(
                userCompleteName,
                errorMessage
            )
        }
    }

    fun validateDocument(docNumberErrorValidationMap: MediatorLiveData<MutableMap<Int, Boolean>>) {
        binding.textLayout.setErrorUserDocNumber(docNumberErrorValidationMap)
    }

    fun setupClick(supportFragmentManager: FragmentManager){

        this.editText.isFocusable = false
        this.editText.isFocusableInTouchMode = false

        this.isClickable = true
        this.isFocusable = true


        val action = {

            Locale.setDefault((Locale("pt", "BR")));

            val today = MaterialDatePicker.todayInUtcMilliseconds()

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(today)
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointBackward.now())
                        .build()
                )
                .build()

            datePicker.show(supportFragmentManager, "datePicker")

            datePicker.addOnNegativeButtonClickListener {
                clearEditTextValue()
            }

            datePicker.addOnPositiveButtonClickListener {

                datePicker.selection?.let { ts ->

                    val dateTime = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        .format(
                            LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(ts),
                                ZoneId.of("UTC"))
                        )

                    setEditTextValue(dateTime)
                }
            }
        }

        this.binding.editText.setOnClickListener {
            action.invoke()
        }

        this.setOnClickListener {
            action.invoke()
        }
    }
}