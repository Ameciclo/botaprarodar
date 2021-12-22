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
import app.igormatos.botaprarodar.databinding.ItemBikeActionMenuBinding
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
        val inflater = LayoutInflater.from(parent.context)
        var binding = ItemBikeActionMenuBinding.inflate(inflater)

        binding.root.layoutParams = ViewGroup.LayoutParams(
            (parent.width * 0.5).toInt(),
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        return BikeActionMenuItemViewHolder(binding)
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

    inner class BikeActionMenuItemViewHolder(binding: ItemBikeActionMenuBinding) : RecyclerView.ViewHolder(binding.root) {

        private val bikeActionTextView = binding.bikeActionTextView
        private val actionImageView = binding.actionImageView
        private val bikeActionMenuContainer = binding.bikeActionMenuContainer


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