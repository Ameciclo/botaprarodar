package app.igormatos.botaprarodar.presentation.adapter

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.extensions.getLastWithdraw
import app.igormatos.botaprarodar.databinding.BicycleCellBinding
import app.igormatos.botaprarodar.domain.model.Bike
import com.bumptech.glide.Glide

class StepOneBikesAdapter(val listener: ReturnBikesAdapterClickListener) :
    ListAdapter<Bike, StepOneBikesAdapter.ReturnBikesAdapterViewHolder>(
        ReturnBikesDiffUtil()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StepOneBikesAdapter.ReturnBikesAdapterViewHolder {
        val binding = BicycleCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReturnBikesAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: StepOneBikesAdapter.ReturnBikesAdapterViewHolder,
        position: Int
    ) {
        holder.bind(currentList[position])
    }

    interface ReturnBikesAdapterClickListener {
        fun bikeOnClickListener(bike: Bike)
    }

    inner class ReturnBikesAdapterViewHolder(val binding: BicycleCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bike: Bike) {
            binding.tvNameBikeItem.text = bike.title()
            binding.tvOrderBikeItem.text =
                itemView.context.getString(
                    R.string.bike_order_with_label,
                    bike.orderNumber.toString()
                )
            binding.tvSeriesBikeItem.text =
                itemView.context.getString(R.string.bike_series_with_label, bike.serialNumber)
            val imageView = binding.ivBikeItem

            if (!bike.isAvailable) {
                Glide.with(itemView.context)
                    .load(bike.iconPath())
                    .into(imageView)

                val colorMatrix = ColorMatrix()
                colorMatrix.setSaturation(0.0f)
                val filter = ColorMatrixColorFilter(colorMatrix)
                imageView.colorFilter = filter

            } else {
                Glide.with(itemView.context)
                    .load(bike.iconPath())
                    .into(imageView)
            }

            binding.root.setOnClickListener {
                listener.bikeOnClickListener(bike)
            }
        }
    }

    class ReturnBikesDiffUtil : DiffUtil.ItemCallback<Bike>() {
        override fun areItemsTheSame(oldItem: Bike, newItem: Bike): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Bike, newItem: Bike): Boolean =
            oldItem == newItem
    }
}