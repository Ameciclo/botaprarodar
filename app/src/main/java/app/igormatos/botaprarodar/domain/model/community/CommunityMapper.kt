package app.igormatos.botaprarodar.domain.model.community

class CommunityMapper {

    fun mapCommunityResponseToCommunity(communityRequest: Map<String, CommunityRequest>): List<Community> {
        return communityRequest.map {
            Community(
                name = it.value.name ?: "",
                address = it.value.address ?: "",
                description = it.value.description ?: "",
                org_name = it.value.orgName ?: "",
                org_email = it.value.orgEmail ?: "",
                id = it.value.id ?: ""
            )
        }
    }
}