package app.igormatos.botaprarodar.common.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.databinding.LayoutBikeActionStepperBinding
import com.google.android.material.card.MaterialCardView

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
    private val items: MutableList<StepConfigType> = arrayListOf()

    init {
        setupLayout()
    }

    private fun setupLayout() {
        binding = LayoutBikeActionStepperBinding.inflate(LayoutInflater.from(context), this, true)
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.BikeActionStepperView, 0, 0)
        currentIconBackground =
            typedArray.getColor(R.styleable.BikeActionStepperView_current_icon_color, -1)
        previousIconBackground =
            typedArray.getColor(R.styleable.BikeActionStepperView_previous_icon_color, -1)
        nextIconBackground =
            typedArray.getColor(R.styleable.BikeActionStepperView_next_icon_color, -1)
        selectBackground =
            typedArray.getColor(R.styleable.BikeActionStepperView_select_background, -1)
        unselectedBackground =
            typedArray.getColor(R.styleable.BikeActionStepperView_unselect_background, -1)

        typedArray.recycle()
    }


    fun addItems(items: List<StepConfigType>) {

        this.items.clear()
        this.items.addAll(items)

        repeat(this.items.size) {
            inflater.inflate(R.layout.item_stepper, binding.stepperContainer)
        }

        binding.stepperContainer.children.forEachIndexed { index, view ->
            val connector = view.findViewById<View>(R.id.stepperConnector)
            val drawable = getDrawable(items[index].icon)

            view.findViewById<ImageView>(R.id.stepperImage).setImageDrawable(drawable)

            when {
                currentPosition < index -> {
                    changeStepStyle(view, nextIconBackground, unselectedBackground)
                    connector.setBackgroundColor(nextIconBackground)
                }
                currentPosition > index -> {
                    changeStepStyle(view, previousIconBackground, selectBackground)
                    connector.setBackgroundColor(selectBackground)
                }
                else -> {
                    changeStepStyle(view, currentIconBackground, unselectedBackground)
                    connector.setBackgroundColor(currentIconBackground)
                }
            }

            binding.stepperTitle.text = context.getString(items[index].title)

        }

        items.firstOrNull()?.let {
            binding.stepperTitle.text = context.getText(it.title)
            removeLastConnector()
        }

    }

    fun setCurrentStep(step: StepConfigType) {
        val result = items.indexOf(step)
        if (result - currentPosition > 0) {
            goToNextStep()
        } else {
            goBackToPreviousStep()
        }
    }

    private fun goToNextStep() {
        val nextPosition = currentPosition + 1

        if (nextPosition < items.size) {
            val nextViewChild = binding.stepperContainer.getChildAt(nextPosition)
            val currentViewChild = binding.stepperContainer.getChildAt(currentPosition)
            val cardImageContainer =
                nextViewChild.findViewById<MaterialCardView>(R.id.stepperImageCard)
            val connector = nextViewChild.findViewById<View>(R.id.stepperConnector)

            changeStepStyle(nextViewChild, currentIconBackground, unselectedBackground)
            changeStepStyle(currentViewChild, previousIconBackground, selectBackground)

            connector.changeConnectorBackgroundColor(selectBackground)
            cardImageContainer.changeStrokeColor(selectBackground)

            currentPosition++
            binding.stepperTitle.text = context.getText(items[currentPosition].title)
        }
    }

    private fun goBackToPreviousStep() {
        val previousPosition = currentPosition - 1

        if (previousPosition >= 0) {
            val previousViewChild = binding.stepperContainer.getChildAt(previousPosition)
            val currentViewChild = binding.stepperContainer.getChildAt(currentPosition)
            val cardImageContainer =
                previousViewChild.findViewById<MaterialCardView>(R.id.stepperImageCard)
            val connector = currentViewChild.findViewById<View>(R.id.stepperConnector)

            changeStepStyle(currentViewChild, nextIconBackground, unselectedBackground)
            changeStepStyle(previousViewChild, currentIconBackground, unselectedBackground)

            connector.changeConnectorBackgroundColor(nextIconBackground)
            cardImageContainer.changeStrokeColor(currentIconBackground)

            currentPosition--
            binding.stepperTitle.text = context.getText(items[currentPosition].title)
        }
    }

    fun completeAllSteps() {
        binding.stepperContainer.children.forEach {
            val connector = it.findViewById<View>(R.id.stepperConnector)
            changeStepStyle(it, previousIconBackground, selectBackground)
            connector.changeConnectorBackgroundColor(selectBackground)
        }

        if (binding.stepperContainer.childCount > 0) {
            currentPosition = items.size - 1
            binding.stepperTitle.text = context.getText(items[currentPosition].title)
        }
    }

    private fun removeLastConnector() {
        val view = binding.stepperContainer.getChildAt(binding.stepperContainer.childCount - 1)
        view.findViewById<View>(R.id.stepperConnector).visibility = View.GONE
    }

    private fun changeStepStyle(view: View, iconBackground: Int, imageBackground: Int) {
        val imageView = view.findViewById<ImageView>(R.id.stepperImage)
        val cardImageContainer = view.findViewById<MaterialCardView>(R.id.stepperImageCard)

        cardImageContainer.setCardBackgroundColor(imageBackground)
        cardImageContainer.strokeColor = iconBackground
        imageView.drawable.setTint(iconBackground)
    }

    private fun getDrawable(@DrawableRes drawableId: Int): Drawable? =
        ContextCompat.getDrawable(context, drawableId)

    private fun View.changeConnectorBackgroundColor(color: Int) {
        setBackgroundColor(color)
    }

    private fun MaterialCardView.changeStrokeColor(color: Int) {
        strokeColor = color
    }

    companion object {
        private const val DEFAULT_DEF_STYLE = 0
    }
}