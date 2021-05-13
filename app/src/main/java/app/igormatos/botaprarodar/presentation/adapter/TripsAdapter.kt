package app.igormatos.botaprarodar.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.ItemActivitiesHistoricBinding
import app.igormatos.botaprarodar.databinding.ItemActivitiesHistoricTitleBinding
import app.igormatos.botaprarodar.presentation.main.trips.TripsItemType
import app.igormatos.botaprarodar.presentation.main.trips.TripsItemType.BikeType
import app.igormatos.botaprarodar.presentation.main.trips.TripsItemType.TitleType
import com.bumptech.glide.Glide

class TripsAdapter(val tripClickListener: TripsAdapterClickListener) :
    ListAdapter<TripsItemType, BaseViewHolder<TripsItemType>>(TripsDiffUtil()) {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<TripsItemType> {
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
        } as BaseViewHolder<TripsItemType>
    }

    override fun onBindViewHolder(holder: BaseViewHolder<TripsItemType>, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is TitleType -> VIEW_TYPE_TITLE
        is BikeType -> VIEW_TYPE_BIKE
        else -> error("Item type not supported")
    }

    interface TripsAdapterClickListener {
        fun tripOnClickListener(bikeId: String?, bikeStatus: String?)
    }

    inner class TitleViewHolder(val binding: ItemActivitiesHistoricTitleBinding) :
        BaseViewHolder<TitleType>(view = binding.root) {

        override fun bind(item: TitleType) {
            binding.tvTitleItemActivitiesHistoric.text = item.title.orEmpty()
        }
    }

    inner class BikeViewHolder(val binding: ItemActivitiesHistoricBinding) :
        BaseViewHolder<BikeType>(view = binding.root) {

        override fun bind(item: BikeType) {
            with(binding) {
                tvNameBikeItemActivities.text = item.bikeActivity.name.orEmpty()
                tvOrderBikeItemActivities.text = itemView.resources.getString(
                    R.string.bike_order_number,
                    item.bikeActivity.orderNumber.toString()
                )
                tvSeriesBikeItem.text = itemView.resources.getString(
                    R.string.bike_series_number,
                    item.bikeActivity.serialNumber.orEmpty()
                )
                if (item.bikeActivity.status == "EMPRÉSTIMO") {
                    tvBikeStatusItemActivities.setBackgroundColor(itemView.resources.getColor(R.color.yellow))
                } else {
                    tvBikeStatusItemActivities.setBackgroundColor(itemView.resources.getColor(R.color.bg_green))
                }
                tvBikeStatusItemActivities.text = item.bikeActivity.status
                Glide.with(itemView.context)
                    .load(item.bikeActivity.photoThumbnailPath)
                    .into(ivBikeItemActvities)
            }
            binding.root.setOnClickListener {
                tripClickListener.tripOnClickListener(
                    item.bikeActivity.id,
                    item.bikeActivity.status
                )
            }
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
}