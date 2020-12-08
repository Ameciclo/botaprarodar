package app.igormatos.botaprarodar.domain.model

import app.igormatos.botaprarodar.domain.model.community.Community

data class UserCommunityInfo(
    val isAdmin:Boolean,
    val communities: List<Community>
)