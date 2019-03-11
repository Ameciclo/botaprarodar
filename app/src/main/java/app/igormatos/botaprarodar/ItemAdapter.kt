package app.igormatos.botaprarodar

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
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
import org.parceler.Parcels

class ItemAdapter(private var withdrawalsList: List<Withdraw>? = null, private var activity: Activity? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemsList: MutableList<Item> = mutableListOf()
    var withdrawalInProgress: Withdraw? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_cell, parent, false)
        return ItemCellViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemsList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, index: Int) {
        val item = itemsList[index]
        val withdrawal = withdrawalsList?.firstOrNull {
            (it.bicycle_id == item.id) && (it.isRent())
        }

        val isAvailable = withdrawal == null

        if (withdrawalInProgress == null) {
            (holder as ItemCellViewHolder).bind(item, isAvailable, withdrawal, withdrawalsList != null, activity)
        } else {
            (holder as ItemCellViewHolder).bindUserSelection(item, withdrawalInProgress!!, activity!!)
        }

    }

    fun updateList(newList: List<Item>) {
        itemsList = newList.toMutableList()
        notifyDataSetChanged()
    }

    fun addItem(item: Item) {
        itemsList.add(0, item)
//        itemsList.add(item)
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
        itemsList.add(0, item)
        notifyDataSetChanged()
    }

    fun updateWithdrawals(withdrawals: List<Withdraw>) {
        withdrawalsList = withdrawals
    }

    class ItemCellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindUserSelection(item: Item, withdrawalInProgress: Withdraw, activity: Activity) {
            itemView.findViewById<TextView>(R.id.cellTitle).text = item.title()
            itemView.findViewById<TextView>(R.id.cellSubtitle).text = item.subtitle()

            val imageView = itemView.findViewById<ImageView>(R.id.cellAvatar)
            Glide.with(itemView.context)
                .load(item.iconPath())
                .into(imageView)

            itemView.setOnClickListener {
                val user = item as User
                withdrawalInProgress.user_id = user.id
                withdrawalInProgress.user = user
                withdrawalInProgress.user_name = user.name
                withdrawalInProgress.user_image_path = user.iconPath()

                val alertDialog = AlertDialog.Builder(itemView.context)
                alertDialog.setTitle("Confirmar retirada?")
                alertDialog.setMessage("Bicicleta: ${withdrawalInProgress.bicycle_name} \nUsuário: ${withdrawalInProgress.user_name}")
                alertDialog.setOnCancelListener {  }
                alertDialog.setPositiveButton("Confirmar") { dialog, which ->
                    withdrawalInProgress.saveRemote {
                        activity.setResult(Activity.RESULT_OK)
                        activity.finish()
                    }
                }.show()
            }

        }

        fun confirmToRemove(item: Item, title: String, subtitle: String = "") {
            val alertDialog = AlertDialog.Builder(itemView.context)
            alertDialog.setTitle(title)
            alertDialog.setMessage(subtitle)
            alertDialog.setOnCancelListener {

            }
            alertDialog.setPositiveButton("Confirmar") { dialog, which -> item.removeRemote() }.show()
        }

        fun bind(item: Item,
                 isAvailable: Boolean,
                 withdrawal: Withdraw? = null,
                 isWithdrawal: Boolean = false,
                 activity: Activity? = null) {

            itemView.findViewById<TextView>(R.id.cellTitle).text = item.title()
            itemView.findViewById<TextView>(R.id.cellSubtitle).text = item.subtitle()


            val imageView = itemView.findViewById<ImageView>(R.id.cellAvatar)

            Glide.with(itemView.context)
                .load(item.iconPath())
                .into(imageView)

            if (item !is Withdraw && !isWithdrawal) {
                itemView.setOnLongClickListener {
                    confirmToRemove(item, "Deseja remover o item?", item.title())
                    true
                }
            }


            if (item is User) {
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, AddUserActivity::class.java)
                    intent.putExtra(USER_EXTRA, Parcels.wrap(User::class.java, item))
                    itemView.context.startActivity(intent)
                }
            }

            if (item is Bicycle && isWithdrawal && activity != null) {
                if (!isAvailable) {
                    itemView.cellContainer.setBackgroundColor(itemView.resources.getColor(R.color.rent))
                }

                itemView.setOnClickListener {
                    if (isAvailable) {
                        val intent = Intent(itemView.context, ChooseUserActivity::class.java)
                        val withdrawalInProgress = Withdraw()
                        withdrawalInProgress.bicycle_name = item.name
                        withdrawalInProgress.bicycle_id = item.id
                        withdrawalInProgress.bicycle_image_path = item.photo_path

                        intent.putExtra(WITHDRAWAL_EXTRA, Parcels.wrap(Withdraw::class.java, withdrawalInProgress))
                        activity.startActivityForResult(intent, Activity.RESULT_OK)
                    } else {
                        val intent = Intent(itemView.context, ReturnBikeActivity::class.java)
                        intent.putExtra(WITHDRAWAL_EXTRA, Parcels.wrap(Withdraw::class.java, withdrawal))
                        activity.startActivityForResult(intent, Activity.RESULT_OK)

                    }
                }
            }

        }
    }
}