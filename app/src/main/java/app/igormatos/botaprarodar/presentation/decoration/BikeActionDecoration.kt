package app.igormatos.botaprarodar.presentation.decoration

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BikeActionDecoration(private val margin: Int, private val listSize: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val currentPosition = parent.getChildLayoutPosition(view)
        setMarginByChildPositionList(currentPosition, outRect, view.context)
    }

    private fun setMarginByChildPositionList(
        currentPosition: Int,
        outRect: Rect,
        context: Context
    ) {

        when (currentPosition) {
            0 -> outRect.right = context.getAnIntDp(margin)
            listSize - 1 -> outRect.left = context.getAnIntDp(margin)
            else -> {
                outRect.right = context.getAnIntDp(margin)
                outRect.left = context.getAnIntDp(margin)
            }
        }
    }

    private fun Context.getAnIntDp(value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value.toFloat(),
            resources.displayMetrics
        ).toInt()
    }
}