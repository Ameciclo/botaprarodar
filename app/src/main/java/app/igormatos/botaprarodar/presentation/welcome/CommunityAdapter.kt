package app.igormatos.botaprarodar.presentation.welcome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.community.Community
import kotlinx.android.synthetic.main.item_community.view.*

class CommunityAdapter(
    private val communityList: ArrayList<Community>, private val click: (Community) -> Unit
) : RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder>(){

    fun updateList(newCommunityList: List<Community>) {
        communityList.clear()
        communityList.addAll(newCommunityList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        return CommunityViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_community, null)
        )
    }

    override fun getItemCount() = communityList.size

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        holder.view.tvCommunityName.apply {
            text = communityList[position].name
            setOnClickListener {
                click(communityList[position])
            }
        }
    }

    class CommunityViewHolder(var view: View) : RecyclerView.ViewHolder(view)

}