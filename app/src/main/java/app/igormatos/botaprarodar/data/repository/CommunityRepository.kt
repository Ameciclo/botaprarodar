package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.Community
import com.google.firebase.database.FirebaseDatabase

class CommunityRepository{

    private val instance = FirebaseDatabase.getInstance()

    private val communitiesPreview = instance.getReference("communities_preview")

    fun addCommunity(community: Community) {
        val communityKey = communitiesPreview.push().key!!
        community.id = communityKey

        communitiesPreview.child(communityKey).setValue(community)
    }


}