package app.igormatos.botaprarodar.common.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.CustomDatePickerBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.custom_edit_text.view.*
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

class CustomDatePicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_DATE_PICKER_PATTERN = "dd/MM/yyyy"
    }

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

    private fun getSelectedDate(): LocalDate {

        var pickerDate = LocalDate.now()

        this.binding.editText.text?.let {
            val dtf = DateTimeFormatter.ofPattern(DEFAULT_DATE_PICKER_PATTERN)

            if (it.isNotBlank()) {
                pickerDate = LocalDate.from(dtf.parse(it.toString()))
            }
        }

        return pickerDate
    }

    fun setupClick(supportFragmentManager: FragmentManager, onValueChanged: (date: String) -> Unit){

        this.editText.isFocusable = false
        this.editText.isFocusableInTouchMode = false
        this.binding.editText.setOnClickListener(showDatePickerAction(supportFragmentManager, onValueChanged))

        this.isClickable = true
        this.isFocusable = true
        this.setOnClickListener(showDatePickerAction(supportFragmentManager, onValueChanged))
    }

    private fun showDatePickerAction(supportFragmentManager: FragmentManager, onValueChanged: (date: String) -> Unit): (View) -> Unit = {

        Locale.setDefault((Locale("pt", "BR")));

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(getSelectedDate().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointBackward.now())
                    .build()
            ).build()

        datePicker.addOnNegativeButtonClickListener(onDatePickerNegativeButtonClick())
        datePicker.addOnPositiveButtonClickListener(onDatePickerPositiveButtonClick(datePicker, onValueChanged))

        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDatePickerNegativeButtonClick(): (View) -> Unit = {
        binding.editText.text?.clear()
    }

    private fun onDatePickerPositiveButtonClick(datePicker: MaterialDatePicker<Long>, onValueChanged: (date: String) -> Unit): (selection: Long) -> Unit = {

        datePicker.selection?.let { ts ->

            val dateTime = DateTimeFormatter.ofPattern(DEFAULT_DATE_PICKER_PATTERN)
                .format(
                    LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(ts),
                        ZoneId.of(ZoneOffset.UTC.toString())
                    )
                )

            setEditTextValue(dateTime)
            onValueChanged.invoke(dateTime)
        }
    }
}