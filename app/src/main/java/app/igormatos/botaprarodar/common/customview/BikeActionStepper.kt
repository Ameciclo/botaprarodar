package app.igormatos.botaprarodar.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import app.igormatos.botaprarodar.R

class BikeActionStepper @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = DEFAULT_DEF_STYLE
): LinearLayout(context, attributeSet, defStyle) {

    init {
        setupLayout()
    }

    private fun setupLayout() {
        LayoutInflater.from(context).inflate(R.layout.layout_bike_action_stepper, this)
    }

    companion object{
        private const val DEFAULT_DEF_STYLE = 0
    }
}