package app.igormatos.botaprarodar.presentation.adapter

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.BicycleCellBinding
import app.igormatos.botaprarodar.domain.model.Bike
import com.bumptech.glide.Glide

class SelectBikeViewHolder(private val onClick: (Bike) -> Unit, val binding: BicycleCellBinding) :
    FormListItemViewHolder<Bike>(view = binding.root) {

    override fun bind(item: Bike) {
        binding.tvNameBikeItem.text = item.title()
        binding.tvOrderBikeItem.text =
            itemView.context.getString(R.string.bike_order_with_label, item.orderNumber.toString())
        binding.tvSeriesBikeItem.text =
            itemView.context.getString(R.string.bike_series_with_label, item.serialNumber)

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