package app.igormatos.botaprarodar.screens.createcommunity

import app.igormatos.botaprarodar.network.Community
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommunityRepository{

    private val instance = FirebaseDatabase.getInstance()

    private val communitiesPreview = instance.getReference("communities_preview")

    fun addCommunity(community: Community) {
        val communityKey = communitiesPreview.push().key!!
        community.id = communityKey

        communitiesPreview.child(communityKey).setValue(community)
    }


}