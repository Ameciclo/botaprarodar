package app.igormatos.botaprarodar.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.model.Withdraw
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class WithdrawAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemsList: MutableList<Withdraw> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cell, parent, false)

        return WithdrawViewHolder(
            view
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

    class WithdrawViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Withdraw) {
            itemView.findViewById<TextView>(R.id.cellTitle).text = item.title()
            itemView.findViewById<TextView>(R.id.cellSubtitle).text = item.subtitle()
            val imageView = itemView.findViewById<ImageView>(R.id.cellImageView)

            val withdrawIcon = if (item.isRent())
                R.drawable.ic_bike_left_24dp
            else
                R.drawable.ic_bike_returned_24dp

            imageView.setImageResource(withdrawIcon)
        }
    }
}