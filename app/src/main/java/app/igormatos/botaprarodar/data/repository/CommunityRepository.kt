package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.api.CommunityApiService
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.model.community.CommunityMapper
import app.igormatos.botaprarodar.domain.model.community.CommunityRequest

class CommunityRepository(
    private val communityApiService: CommunityApiService,
    private val communityMapper: CommunityMapper
) {

    suspend fun getCommunitiesPreview(): List<Community> {
        val communityResponse = communityApiService.getCommunities()
        return communityMapper.mapCommunityResponseToCommunity(communityResponse)
    }

    suspend fun addCommunity(community: CommunityRequest): String {
        return communityApiService.addCommunity(community).name
    }

}