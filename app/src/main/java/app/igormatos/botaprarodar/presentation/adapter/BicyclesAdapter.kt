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

class BicyclesAdapter(private val bikeListener: BicycleAdapterListener) :
    ListAdapter<Bike, BicyclesAdapter.BicycleViewHolder>(
        UsersDiffUtil()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BicycleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bicycle_cell, parent, false)
        return BicycleViewHolder(view)
    }

    override fun onBindViewHolder(holder: BicycleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BicycleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(bike: Bike) {
            itemView.findViewById<TextView>(R.id.tv_name_bike_item).text = bike.title()
            itemView.findViewById<TextView>(R.id.tv_order_bike_item).text =
                itemView.context.getString(R.string.bike_order_number, bike.order_number.toString())
            itemView.findViewById<TextView>(R.id.tv_series_bike_item).text =
                itemView.context.getString(R.string.bike_series_number, bike.serial_number)
            val imageView = itemView.findViewById<ImageView>(R.id.iv_bike_item)

            itemView.setOnClickListener {
                bikeListener.onBicycleClicked(bike)
            }

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
        }
    }

    interface BicycleAdapterListener {
        fun onBicycleClicked(bike: Bike)
        fun onBicycleLongClicked(bike: Bike): Boolean
    }

    class UsersDiffUtil : DiffUtil.ItemCallback<Bike>() {
        override fun areItemsTheSame(oldItem: Bike, newItem: Bike): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Bike, newItem: Bike): Boolean =
            oldItem == newItem
    }
}