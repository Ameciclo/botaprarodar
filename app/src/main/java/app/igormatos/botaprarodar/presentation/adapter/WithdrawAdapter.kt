package app.igormatos.botaprarodar.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.ItemCellBinding
import app.igormatos.botaprarodar.domain.model.Withdraw

class WithdrawAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemsList: MutableList<Withdraw> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCellBinding.inflate(inflater)

        return WithdrawViewHolder(
            binding
        )
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun getItemCount(): Int {
        return itemsList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, index: Int) {
        val item = itemsList[index]
        (holder as WithdrawViewHolder).bind(item)
    }

    fun addItem(withdraw: Withdraw) {
        itemsList.add(0, withdraw)
        notifyDataSetChanged()
    }

    fun removeItem(item: Withdraw) {
        itemsList.remove(item)
        notifyDataSetChanged()
    }

    fun updateItem(item: Withdraw) {
        val index = itemsList.indexOfFirst { it.id == item.id }
        itemsList[index] = item

        notifyItemChanged(index)
    }

    class WithdrawViewHolder(val binding: ItemCellBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(item: Withdraw) {
            binding.cellTitle.text = item.title()
            binding.cellSubtitle.text = item.subtitle()
            val imageView = binding.cellImageView

            val withdrawIcon = if (item.isRent())
                R.drawable.ic_bike_left_24dp
            else
                R.drawable.ic_bike_returned_24dp

            imageView.setImageResource(withdrawIcon)
        }
    }
}