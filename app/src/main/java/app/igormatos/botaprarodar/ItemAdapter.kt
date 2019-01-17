package app.igormatos.botaprarodar

import android.content.Intent
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
import app.igormatos.botaprarodar.model.Withdraw
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_cell.view.*
import org.jetbrains.anko.backgroundColor
import org.parceler.Parcels

class ItemAdapter(private var withdrawalsList: List<Withdraw>? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var itemsList: MutableList<Item> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_cell, parent, false)
        return ItemCellViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemsList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, index: Int) {
        val bicycle = itemsList[index]
        val withdrawal = withdrawalsList?.firstOrNull {
            (it.bicycle_id == bicycle.id) && (it.return_date.isNullOrEmpty())
        }

        val isAvailable = withdrawal == null

        (holder as ItemCellViewHolder).bind(bicycle, isAvailable)
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

    fun updateItem(item: Item) {
        val index = itemsList.indexOfFirst { it -> it.id == item.id }
        itemsList.removeAt(index)
        itemsList.add(index, item)
        notifyItemChanged(index)
    }

    fun updateWithdrawals(withdrawals: List<Withdraw>) {
        withdrawalsList = withdrawals
    }

    class ItemCellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Item, isAvailable: Boolean) {
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

            if (item is User) {
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, AddUserActivity::class.java)
                    intent.putExtra(USER_EXTRA, Parcels.wrap(User::class.java, item))
                    itemView.context.startActivity(intent)
                }
            }

            if (item is Bicycle) {
                if (!isAvailable) {
                    itemView.cellContainer.setBackgroundColor(itemView.resources.getColor(R.color.rent))
                }

                itemView.setOnClickListener {
                    if (isAvailable) {
                        Toast.makeText(it.context, "Está disponivel", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(it.context, "Não está disponível", Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }
    }
}