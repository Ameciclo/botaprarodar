package app.igormatos.botaprarodar.domain.model

import app.igormatos.botaprarodar.data.network.Community

data class UserCommunityInfo(
    val isAdmin:Boolean,
    val communities: List<Community>
)