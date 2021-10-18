package app.igormatos.botaprarodar.presentation.welcome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.ItemCommunityBinding
import app.igormatos.botaprarodar.domain.model.community.Community

class CommunityAdapter(
    private val communityList: ArrayList<Community>,
    private val click: (Community) -> Unit
) : RecyclerView.Adapter<CommunityAdapter.CommunityViewHolder>() {

    fun updateList(newCommunityList: List<Community>) {
        communityList.clear()
        communityList.addAll(newCommunityList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val binding =
            ItemCommunityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommunityViewHolder(binding)
    }

    override fun getItemCount() = communityList.size

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val community = communityList[position]
        holder.bind(community)
    }

    inner class CommunityViewHolder(val binding: ItemCommunityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(community: Community) {
            binding.apply {
                tvCommunityName.text = community.name
                tvCommunityDescription.text =
                    root.context.getString(R.string.community_description, community.description)
                tvCommunityAdress.text =
                    root.context.getString(R.string.community_address, community.address)
                root.setOnClickListener {
                    click(community)
                }
            }
        }
    }

}