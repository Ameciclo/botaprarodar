package app.igormatos.botaprarodar.common.customview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.children
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.databinding.LayoutBikeActionStepperBinding

class BikeActionStepperView @JvmOverloads constructor(
    context: Context,
    private val attributeSet: AttributeSet? = null,
    defStyle: Int = DEFAULT_DEF_STYLE
) : FrameLayout(context, attributeSet, defStyle) {

    private lateinit var binding: LayoutBikeActionStepperBinding
    private val inflater = LayoutInflater.from(context)

    //attrs
    private var currentIconBackground: Int = -1
    private var previousIconBackground: Int = -1
    private var nextIconBackground: Int = -1
    private var selectBackground: Int = -1
    private var unselectedBackground = -1
    private var currentPosition = 0

    init {
        setupLayout()
    }

    private fun setupLayout() {
        binding = LayoutBikeActionStepperBinding.inflate(LayoutInflater.from(context), this, true)
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.BikeActionStepperView, 0, 0)
        currentIconBackground = typedArray.getColor(R.styleable.BikeActionStepperView_current_icon_color, -1)
        previousIconBackground = typedArray.getColor(R.styleable.BikeActionStepperView_previous_icon_color, -1)
        nextIconBackground = typedArray.getColor(R.styleable.BikeActionStepperView_next_icon_color, -1)
        selectBackground = typedArray.getColor(R.styleable.BikeActionStepperView_select_background, -1)
        unselectedBackground = typedArray.getColor(R.styleable.BikeActionStepperView_unselect_background, -1)

        typedArray.recycle()
    }

    fun addItems(items: List<StepConfigType>) {

        repeat(items.size) {
            inflater.inflate(R.layout.item_stepper, binding.stepperContainer)
        }

        binding.stepperContainer.children.forEachIndexed { index, view ->
            val icon = view.findViewById<ImageView>(R.id.stepperImage)
            val iconDrawable = AppCompatResources.getDrawable(context, items[index].icon)

            when {
                currentPosition < index -> setDrawableColor(iconDrawable, Color.RED)
                currentPosition > index -> setDrawableColor(iconDrawable, Color.GREEN)
                else -> setDrawableColor(iconDrawable, Color.GRAY)
            }

            icon.setBackgroundResource(items[index].icon)
        }

        removeLastConnector()
    }

    private fun setDrawableColor(drawable: Drawable?, color: Int) {
        drawable?.let {
            val wrapDrawable = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(wrapDrawable, color)
        }
    }

    fun goToNextStep() {

    }

    fun goToPreviousStep() {

    }

    private fun removeLastConnector() {
        val view = binding.stepperContainer.getChildAt(binding.stepperContainer.childCount - 1)
        view.findViewById<View>(R.id.stepperConnector).visibility = View.GONE
    }

    companion object {
        private const val DEFAULT_DEF_STYLE = 0
    }
}