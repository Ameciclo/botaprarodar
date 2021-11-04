package app.igormatos.botaprarodar.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.UsersItemBinding
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.extensions.gone
import com.brunotmgomes.ui.extensions.loadPathOnCircle
import com.brunotmgomes.ui.extensions.visible

class SelectUserViewHolder(
    private val onClick: (User) -> Unit,
    private val binding: UsersItemBinding
) : FormListItemViewHolder<User>(view = binding.root) {

    override fun bind(item: User) {
        binding.tvNameUserItem.text = item.title()
        binding.tvRegisteredSinceUserItem.text =
            itemView.resources.getString(R.string.user_created_since, item.createdDate)

        with(binding.ivUserItem) {
            loadPathOnCircle(item.iconPath())
        }

        if (item.hasActiveWithdraw)
            binding.tvActiveWithdraw.visible()
        else
            binding.tvActiveWithdraw.gone()

        if (item.isBlocked)
            binding.userBlockedIcon.visible()
        else
            binding.userBlockedIcon.gone()

        itemView.setOnClickListener {
            if (item.hasActiveWithdraw.not())
                onClick(item)
        }
    }

}

abstract class FormListItemViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}
