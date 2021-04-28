package app.igormatos.botaprarodar.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.ItemActivitiesHistoricBinding
import app.igormatos.botaprarodar.databinding.ItemActivitiesHistoricTitleBinding
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.adapter.TripsAdapter.TripsLayoutType
import app.igormatos.botaprarodar.presentation.adapter.TripsAdapter.TripsLayoutType.BikeType
import app.igormatos.botaprarodar.presentation.adapter.TripsAdapter.TripsLayoutType.TitleType

abstract class ViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(item: T) {
    }
}

class TripsAdapter : ListAdapter<TripsLayoutType, ViewHolder<TripsLayoutType>>(TripsDiffUtil()) {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<TripsLayoutType> {
        return when (viewType) {
            VIEW_TYPE_TITLE -> {
                val binding = ItemActivitiesHistoricTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                TitleViewHolder(binding)
            }
            VIEW_TYPE_BIKE -> {
                val binding = ItemActivitiesHistoricBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                BikeViewHolder(binding)
            }
            else -> error("Invalid type")
        } as ViewHolder<TripsLayoutType>
    }

    override fun onBindViewHolder(holder: ViewHolder<TripsLayoutType>, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is TitleType -> VIEW_TYPE_TITLE
        is BikeType -> VIEW_TYPE_BIKE
        else -> error("Item type not supported")
    }

    inner class TitleViewHolder(val binding: ItemActivitiesHistoricTitleBinding) :
        ViewHolder<TitleType>(view = binding.root) {

        override fun bind(item: TitleType) {
            binding.tvTitleItemActivitiesHistoric.text = item.title.orEmpty()
        }
    }

    inner class BikeViewHolder(val binding: ItemActivitiesHistoricBinding) :
        ViewHolder<BikeType>(view = binding.root) {

        override fun bind(item: BikeType) {
            binding.tvNameBikeItemActivities.text = item.bike.name.orEmpty()
            binding.tvOrderBikeItemActivities.text = item.bike.orderNumber.toString()
            binding.tvSeriesBikeItem.text = item.bike.serialNumber.orEmpty()
        }
    }

    companion object {
        private const val VIEW_TYPE_TITLE = R.layout.item_activities_historic_title
        private const val VIEW_TYPE_BIKE = R.layout.item_activities_historic
    }

    class TripsDiffUtil<T> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem == newItem

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
            oldItem == newItem
    }

    sealed class TripsLayoutType {
        data class TitleType(val title: String) : TripsLayoutType()
        data class BikeType(val bike: Bike) : TripsLayoutType()
    }


}