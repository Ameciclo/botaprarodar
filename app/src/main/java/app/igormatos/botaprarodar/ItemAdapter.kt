package app.igormatos.botaprarodar

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import app.igormatos.botaprarodar.model.Bicycle
import app.igormatos.botaprarodar.model.Item
import app.igormatos.botaprarodar.model.User
import com.bumptech.glide.Glide

class ItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var itemsList: MutableList<Item> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_cell, parent, false)
        return ItemCellViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemsList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, index: Int) {
        (holder as ItemCellViewHolder).bind(itemsList[index])
    }

    fun updateList(newList: List<Item>) {
        itemsList = newList.toMutableList()
        notifyDataSetChanged()
    }

    fun addItem(item: Item) {
        itemsList.add(item)
        notifyDataSetChanged()
//        notifyItemInserted(itemsList.size - 1)
    }

    fun removeItem(item: Item) {
        val index = itemsList.indexOfFirst { it -> it.id == item.id }
        itemsList.removeAt(index)
        notifyItemRemoved(index)
    }

    class ItemCellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Item) {
            itemView.findViewById<TextView>(R.id.cellTitle).text = item.title()
            itemView.findViewById<TextView>(R.id.cellSubtitle).text = item.subtitle()

            val imageView = itemView.findViewById<ImageView>(R.id.cellAvatar)

            Glide.with(itemView.context)
                .load(item.iconPath())
                .into(imageView)

            itemView.setOnLongClickListener {
                item.removeRemote()
                Toast.makeText(itemView.context, "Item removido", Toast.LENGTH_SHORT).show()
                true
            }

        }
    }
}