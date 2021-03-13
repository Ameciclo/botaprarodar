package app.igormatos.botaprarodar.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import app.igormatos.botaprarodar.databinding.BicycleCellBinding
import app.igormatos.botaprarodar.domain.model.Bike

class SelectBikeListAdapter(private val onClick: (Bike) -> Unit) :
    ListAdapter<Bike, FormListItemViewHolder<Bike>>(
        ReturnBikesDiffUtil()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FormListItemViewHolder<Bike> {
        val binding = BicycleCellBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SelectBikeViewHolder(onClick, binding)
    }

    override fun onBindViewHolder(
        holder: FormListItemViewHolder<Bike>,
        position: Int
    ) {
        holder.bind(currentList[position])
    }


    class ReturnBikesDiffUtil : DiffUtil.ItemCallback<Bike>() {
        override fun areItemsTheSame(oldItem: Bike, newItem: Bike): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Bike, newItem: Bike): Boolean =
            oldItem == newItem
    }

}