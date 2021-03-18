package app.igormatos.botaprarodar.presentation.adapter

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.UsersItemBinding
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.extensions.loadPathOnCircle
import com.bumptech.glide.Glide

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

        itemView.setOnClickListener {
            onClick(item)
        }
    }

}

abstract class FormListItemViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}
