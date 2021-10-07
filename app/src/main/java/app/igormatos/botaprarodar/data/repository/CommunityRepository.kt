package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.network.api.CommunityApiService
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.model.community.CommunityMapper
import app.igormatos.botaprarodar.domain.model.community.CommunityRequest
import java.net.UnknownHostException

class CommunityRepository(
    private val communityApiService: CommunityApiService,
    private val communityMapper: CommunityMapper
) {

    //TODO verify this
    suspend fun getCommunities(): List<Community> {
        val communityListResponse = communityApiService.getCommunities()
        return mutableListOf()
    }

    suspend fun getCommunitiesPreview(): List<Community> {
        try {
            val communityResponse = communityApiService.getCommunitiesPreview()
            return communityMapper.mapCommunityResponseToCommunity(communityResponse)
        } catch (e: UnknownHostException) {
            throw UserAdminErrorException.AdminNetwork
        }
    }

    suspend fun addCommunity(community: CommunityRequest): String {
        return communityApiService.addCommunity(community).name
    }

}