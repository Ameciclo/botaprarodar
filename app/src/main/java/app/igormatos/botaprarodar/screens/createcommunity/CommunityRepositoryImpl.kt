package app.igormatos.botaprarodar.screens.createcommunity

import app.igormatos.botaprarodar.network.Community
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommunityRepositoryImpl : CommunityRepository{

    override val instance = FirebaseDatabase.getInstance()

    override val communitiesPreview = instance.getReference("communities_preview")

    override fun addCommunity(community: Community) {
        val communityKey = communitiesPreview.push().key!!
        community.id = communityKey

        communitiesPreview.child(communityKey).setValue(community)
    }


}