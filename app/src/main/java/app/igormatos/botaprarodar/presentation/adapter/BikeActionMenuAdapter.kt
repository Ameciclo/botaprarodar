package app.igormatos.botaprarodar.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BikeActionsMenuType
import com.google.android.material.card.MaterialCardView

class BikeActionMenuAdapter(
    private val itemList: MutableList<BikeActionsMenuType> = arrayListOf(),
    private val navigateToReturnBikeActivity: () -> Unit,
    private val navigateToBikeWithdrawActivity: () -> Unit
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
        holder.onItemClick(itemList[position])
    }

    fun updateItems(list: List<BikeActionsMenuType>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    inner class BikeActionMenuItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val bikeActionTextView = itemView.findViewById<TextView>(R.id.bikeActionTextView)
        private val actionImageView = itemView.findViewById<ImageView>(R.id.actionImageView)
        private val bikeActionMenuContainer =
            itemView.findViewById<MaterialCardView>(R.id.bikeActionMenuContainer)


        fun bind(item: BikeActionsMenuType) {
            bikeActionTextView.text = itemView.context.getString(item.stringId)
            actionImageView.setImageResource(item.icon)
        }

        fun onItemClick(bikeActionsMenuType: BikeActionsMenuType) {
            bikeActionMenuContainer.setOnClickListener {
                when (bikeActionsMenuType) {
                    BikeActionsMenuType.BORROW -> {
                        navigateToBikeWithdrawActivity.invoke()
                    }
                    BikeActionsMenuType.RETURN -> {
                        navigateToReturnBikeActivity.invoke()
                    }
                }
            }
        }
    }
}