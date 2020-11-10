package app.igormatos.botaprarodar.data.model

import app.igormatos.botaprarodar.network.Community

data class UserCommunityInfo(
    val isAdmin:Boolean,
    val communities: List<Community>
)