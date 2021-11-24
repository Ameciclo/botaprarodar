package app.igormatos.botaprarodar.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.UsersItemBinding
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.extensions.gone
import com.brunotmgomes.ui.extensions.loadPathOnCircle
import com.brunotmgomes.ui.extensions.visible

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

    inner class UsersViewHolder(val binding: UsersItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.tvNameUserItem.text = user.title()
            binding.tvUserPhoneNumber.text = user.telephoneHide4Chars()
            user.profilePictureThumbnail?.let { profileImage ->
                binding.ivUserItem.loadPathOnCircle(profileImage)
            }

            binding.root.setOnClickListener { listener.onUserClicked(user) }

            userIsBlocked(user)
            auxiliarText(user)
        }

        private fun userIsBlocked(user: User) {
            if (user.isBlocked)
                binding.userBlockedIcon.visible()
            else
                binding.userBlockedIcon.gone()
        }

        private fun auxiliarText(user: User) {
            if (user.hasActiveWithdraw) {
                binding.tvActiveWithdraw.visible()
                binding.tvUserPhoneNumber.gone()
            } else {
                binding.tvActiveWithdraw.gone()
                binding.tvUserPhoneNumber.visible()
            }
        }
    }

    class UsersDiffUtil : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem == newItem
    }
}