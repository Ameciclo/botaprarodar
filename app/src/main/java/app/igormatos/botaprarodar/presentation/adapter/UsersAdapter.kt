package app.igormatos.botaprarodar.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.Item
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.extensions.loadPathOnCircle

private const val INDEX_INVALID = -1

class UsersAdapter(private val listener: UsersAdapterListener) :
    ListAdapter<User, UsersAdapter.UsersViewHolder>(UsersDiffUtil()), Filterable {

    private val users = mutableListOf<User>()
    var filteredList = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.users_item, parent, false)
        return UsersViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchWord = constraint.toString()

                filteredList = if (searchWord.isEmpty()) {
                    users
                } else {
                    users.filter { item ->
                        item.name!!.contains(searchWord, true) ||
                                item.doc_number.toString().contains(searchWord, true)
                    }.toMutableList()
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as MutableList<User>
                submitList(filteredList)
            }
        }

    }

    interface UsersAdapterListener {
        fun onUserClicked(user: User)
    }

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: User) {
            itemView.findViewById<TextView>(R.id.tv_name_user_item).text = user.title()
            user.profile_picture_thumbnail?.let { profileImage ->
                itemView.findViewById<ImageView>(R.id.iv_user_item).loadPathOnCircle(
                    profileImage
                )
            }
            itemView.findViewById<TextView>(R.id.tv_registered_since_user_item).text =
                itemView.context.getString(R.string.user_created_since, user.created_date)

            itemView.setOnClickListener {
                listener.onUserClicked(user)
            }
        }
    }

    class UsersDiffUtil : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem == newItem
    }

    fun Int.validateIndex() = this != INDEX_INVALID
}
