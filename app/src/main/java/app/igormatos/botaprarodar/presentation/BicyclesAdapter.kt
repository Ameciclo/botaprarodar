package app.igormatos.botaprarodar.presentation

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.Bike
import com.bumptech.glide.Glide

class BicyclesAdapter(
    private val bicycleAdapterListener: BicycleAdapterListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BicycleAdapterListener {

    var itemsList: MutableList<Bike> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bicycle_cell, parent, false)
        return BicycleViewHolder(bicycleAdapterListener, view)
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getItemCount(): Int {
        return itemsList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, index: Int) {
        val item = itemsList[index]
        (holder as BicycleViewHolder).bind(item)
    }

    fun addItem(item: Bike) {
        itemsList.add(0, item)
        notifyDataSetChanged()
    }

    fun removeItem(item: Bike) {
        val index = itemsList.indexOfFirst { it.id == item.id }
        itemsList.removeAt(index)
        notifyItemRemoved(index)
    }

    fun updateItem(item: Bike) {
        val index = itemsList.indexOfFirst { it.id == item.id }
        itemsList[index] = item
        notifyItemChanged(index)
    }

    class BicycleViewHolder(
        private val bicycleAdapterListener: BicycleAdapterListener,
        itemView: View
    ) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(bike: Bike) {
            itemView.findViewById<TextView>(R.id.tv_name_bike_item).text = bike.title()
            itemView.findViewById<TextView>(R.id.tv_order_bike_item).text =
                itemView.context.getString(R.string.bike_order_number, bike.order_number.toString())
            itemView.findViewById<TextView>(R.id.tv_series_bike_item).text =
                itemView.context.getString(R.string.bike_series_number, bike.serial_number)
            val imageView = itemView.findViewById<ImageView>(R.id.iv_bike_item)

            itemView.setOnLongClickListener {
                bicycleAdapterListener.onBicycleLongClicked(bike)
            }

            itemView.setOnClickListener {
                bicycleAdapterListener.onBicycleClicked(bike)
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

    override fun onBicycleClicked(bike: Bike) {
        bicycleAdapterListener.onBicycleClicked(bike)
    }

    override fun onBicycleLongClicked(bike: Bike): Boolean {
        return bicycleAdapterListener.onBicycleLongClicked(bike)
    }
}