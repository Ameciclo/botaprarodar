package app.igormatos.botaprarodar.domain.model.community

import app.igormatos.botaprarodar.common.extensions.convertToList

class CommunityMapper {

    fun mapCommunityResponseToCommunity(communityRequest: Map<String, CommunityRequest>): List<Community> {
        val list: MutableList<CommunityRequest> = communityRequest.convertToList()
        return list.map {
            Community(
                name = it.name ?: "",
                address = it.address ?: "",
                description = it.description ?: "",
                org_name = it.orgName ?: "",
                org_email = it.orgEmail ?: "",
                id = it.id ?: ""
            )
        }
    }
}