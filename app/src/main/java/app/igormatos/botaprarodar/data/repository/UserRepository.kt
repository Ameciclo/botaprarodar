package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.UserApiService
import app.igormatos.botaprarodar.domain.model.user.UserRequest

class UserRepository(
    private val userApi: UserApiService,
    private val preferencesModule: SharedPreferencesModule
) {

    private val communityId = preferencesModule.getJoinedCommunity().id

    suspend fun saveUser(userRequest: UserRequest): String {
        return userApi.addUser(userRequest, communityId).name
    }

    suspend fun updateUser(userRequest: UserRequest, userId: String?): String {
        return userApi.updateUser(userRequest, userId, communityId).name
    }

}