package app.igormatos.botaprarodar.presentation.adapter

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
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
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bicycle_cell, parent, false)
        return ReturnBikesAdapterViewHolder(view)
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

    inner class ReturnBikesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(bike: Bike) {
            itemView.findViewById<TextView>(R.id.tv_name_bike_item).text = bike.title()
            itemView.findViewById<TextView>(R.id.tv_order_bike_item).text =
                itemView.context.getString(R.string.bike_order_with_label, bike.orderNumber.toString())
            itemView.findViewById<TextView>(R.id.tv_series_bike_item).text =
                itemView.context.getString(R.string.bike_series_with_label, bike.serialNumber)
            val imageView = itemView.findViewById<ImageView>(R.id.iv_bike_item)

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

            itemView.setOnClickListener {
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