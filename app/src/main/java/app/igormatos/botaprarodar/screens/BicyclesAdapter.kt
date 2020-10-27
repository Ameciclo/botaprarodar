package app.igormatos.botaprarodar.screens

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.model.Bicycle
import com.bumptech.glide.Glide

class BicyclesAdapter(
    private val bicycleAdapterListener: BicycleAdapterListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BicycleAdapterListener {

    var itemsList: MutableList<Bicycle> = mutableListOf()

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

    fun addItem(item: Bicycle) {
        itemsList.add(0, item)
        notifyDataSetChanged()
    }

    fun removeItem(item: Bicycle) {
        val index = itemsList.indexOfFirst { it.id == item.id }
        itemsList.removeAt(index)
        notifyItemRemoved(index)
    }

    fun updateItem(item: Bicycle) {
        val index = itemsList.indexOfFirst { it.id == item.id }
        itemsList[index] = item
        notifyItemChanged(index)
    }

    class BicycleViewHolder(
        private val bicycleAdapterListener: BicycleAdapterListener,
        itemView: View
    ) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(bicycle: Bicycle) {
            itemView.findViewById<TextView>(R.id.cellTitle).text = bicycle.title()
            itemView.findViewById<TextView>(R.id.cellSubtitle).text = bicycle.subtitle()
            val imageView = itemView.findViewById<ImageView>(R.id.cellImageView)

            itemView.setOnLongClickListener {
                bicycleAdapterListener.onBicycleLongClicked(bicycle)
            }

            itemView.setOnClickListener {
                bicycleAdapterListener.onBicycleClicked(bicycle)
            }

            if (!bicycle.isAvailable) {
                Glide.with(itemView.context)
                    .load(bicycle.iconPath())
                    .into(imageView)

                val colorMatrix = ColorMatrix()
                colorMatrix.setSaturation(0.0f)
                val filter = ColorMatrixColorFilter(colorMatrix)
                imageView.colorFilter = filter

            } else {
                Glide.with(itemView.context)
                    .load(bicycle.iconPath())
                    .into(imageView)
            }
        }
    }

    override fun onBicycleClicked(bicycle: Bicycle) {
        bicycleAdapterListener.onBicycleClicked(bicycle)
    }

    override fun onBicycleLongClicked(bicycle: Bicycle): Boolean {
        return bicycleAdapterListener.onBicycleLongClicked(bicycle)
    }
}