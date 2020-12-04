package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.CommunityApiService
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.model.community.CommunityBody
import app.igormatos.botaprarodar.domain.model.community.CommunityMapper

class CommunityRepository(
    private val communityApiService: CommunityApiService,
    private val communityMapper: CommunityMapper
) {

    suspend fun getCommunities() : List<Community> {
        val communityListResponse = communityApiService.getCommunities()
        return communityMapper.mapCommunityResponseToCommunity(communityListResponse)
    }

    suspend fun addCommunity(community: CommunityBody) : String {
        return communityApiService.addCommunity(community).name
    }
}