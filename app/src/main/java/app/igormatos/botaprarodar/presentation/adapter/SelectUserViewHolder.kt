package app.igormatos.botaprarodar.presentation.adapter

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.databinding.UserCellBinding
import app.igormatos.botaprarodar.domain.model.User
import com.bumptech.glide.Glide

class SelectUserViewHolder(
    private val onClick: (User) -> Unit,
    private val binding: UserCellBinding
) : FormListItemViewHolder<User>(view = binding.root) {

    override fun bind(item: User) {
        binding.userName.text = item.title()

        with(binding.ivBikeItem) {
            if (!item.isAvailable) {
                Glide.with(itemView.context)
                    .load(item.iconPath())
                    .into(this)

                val colorMatrix = ColorMatrix()
                colorMatrix.setSaturation(0.0f)
                val filter = ColorMatrixColorFilter(colorMatrix)
                colorFilter = filter

            } else {
                Glide.with(itemView.context)
                    .load(item.iconPath())
                    .into(this)
            }
        }

        itemView.setOnClickListener {
            onClick(item)
        }
    }

}

abstract class FormListItemViewHolder<T>(view: View): RecyclerView.ViewHolder(view){
    abstract fun bind(item: T)
}
