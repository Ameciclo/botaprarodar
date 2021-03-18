package app.igormatos.botaprarodar.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import app.igormatos.botaprarodar.databinding.UsersItemBinding
import app.igormatos.botaprarodar.domain.model.User

class SelectUserListAdapter(private val onClick: (User) -> Unit) :
    ListAdapter<User, FormListItemViewHolder<User>>(
        ReturnBikesDiffUtil()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FormListItemViewHolder<User> {
        val binding = UsersItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SelectUserViewHolder(onClick, binding)
    }

    override fun onBindViewHolder(
        holder: FormListItemViewHolder<User>,
        position: Int
    ) {
        holder.bind(currentList[position])
    }


    class ReturnBikesDiffUtil : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem == newItem
    }

}