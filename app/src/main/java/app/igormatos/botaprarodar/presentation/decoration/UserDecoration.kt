package app.igormatos.botaprarodar.presentation.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

private const val FIRST_ITEM_INDEX = 0

class UserDecoration(val marginTop: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == FIRST_ITEM_INDEX) {
            outRect.top = marginTop
        }
    }

}