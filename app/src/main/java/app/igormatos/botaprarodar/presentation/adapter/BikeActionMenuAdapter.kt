package app.igormatos.botaprarodar.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BikeActionsMenuType

class BikeActionMenuAdapter(
    private val itemList: MutableList<BikeActionsMenuType>
) : RecyclerView.Adapter<BikeActionMenuAdapter.BikeActionMenuItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BikeActionMenuItemViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bike_action_menu, parent, false)

        root.layoutParams = ViewGroup.LayoutParams(
            (parent.width * 0.5).toInt(),
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        return BikeActionMenuItemViewHolder(root)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: BikeActionMenuItemViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    class BikeActionMenuItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val bikeActionTextView = itemView.findViewById<TextView>(R.id.bikeActionTextView)
        private val borrowImageView = itemView.findViewById<ImageView>(R.id.borrowImageView)
        private val returnImageView = itemView.findViewById<ImageView>(R.id.returnImageView)

        fun bind(item: BikeActionsMenuType) {
            bikeActionTextView.text = itemView.context.getString(item.stringId)

            when (item) {
                BikeActionsMenuType.BORROW -> showActionImageVisibility(View.VISIBLE, View.INVISIBLE)
                BikeActionsMenuType.RETURN -> showActionImageVisibility(View.INVISIBLE, View.VISIBLE)
            }
        }

        private fun showActionImageVisibility(
            visibilityBorrowImageView: Int,
            visibilityReturnImageView: Int
        ) {
            borrowImageView.visibility = visibilityBorrowImageView
            returnImageView.visibility = visibilityReturnImageView
        }
    }
}