package app.igormatos.botaprarodar.screens.createcommunity

import app.igormatos.botaprarodar.network.Community
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

interface CommunityRepository {

    val instance: FirebaseDatabase
    val communitiesPreview: DatabaseReference

    fun addCommunity(community: Community)

}