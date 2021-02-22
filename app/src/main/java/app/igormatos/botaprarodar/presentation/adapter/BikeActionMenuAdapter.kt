package app.igormatos.botaprarodar.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R

class BikeActionMenuAdapter(
    private val itemList:List<String> = arrayListOf("EMPRESTAR", "DEVOLVER", "DEVOLVER")
): RecyclerView.Adapter<BikeActionMenuAdapter.BikeActionMenuItemViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BikeActionMenuItemViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.item_bike_action_menu, parent, false)
        return BikeActionMenuItemViewHolder(root)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: BikeActionMenuItemViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    class BikeActionMenuItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(item: String){
            itemView.findViewById<TextView>(R.id.bikeActionTextView).text = item
        }
    }
}