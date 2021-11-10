
package app.igormatos.botaprarodar.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.UsersItemBinding
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.extensions.loadPathOnCircle

class UsersAdapter(private val listener: UsersAdapterListener) :
    ListAdapter<User, UsersAdapter.UsersViewHolder>(UsersDiffUtil()), Filterable {

    private var users = mutableListOf<User>()
    var filteredList = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {

        val layoutInflater =
            LayoutInflater.from(parent.context)
        val binding = UsersItemBinding.inflate(layoutInflater)

        users = currentList
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchWord = constraint.toString()

                filteredList = if (searchWord.isEmpty())
                    users
                else
                    applyFilter(searchWord)

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

    private fun applyFilter(searchWord: String): MutableList<User> {
        return users.filter { user ->
            user.run {
                contains(name, searchWord) ||
                        contains(docNumber.toString(), searchWord)
            }
        }.toMutableList()
    }

    private fun contains(word: String?, searchWord: String): Boolean {
        return word.toString().contains(searchWord, true)
    }

    interface UsersAdapterListener {
        fun onUserClicked(user: User)
    }

    inner class UsersViewHolder(val binding: UsersItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.tvNameUserItem.text = user.title()
            user.profilePictureThumbnail?.let { profileImage ->
                binding.ivUserItem.loadPathOnCircle(profileImage)
            }
            binding.tvRegisteredSinceUserItem.text = itemView.context.getString(R.string.user_created_since, user.createdDate)

            binding.root.setOnClickListener { listener.onUserClicked(user) }

            if (user.isBlocked)
                binding.userBlockedIcon.visibility = View.VISIBLE
            else
                binding.userBlockedIcon.visibility = View.GONE
        }
    }

    class UsersDiffUtil : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem == newItem
    }
}