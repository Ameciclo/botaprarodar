package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.model.UserRequest
import app.igormatos.botaprarodar.data.network.api.UserApi

class UserRepository(private val userApi: UserApi) {

    suspend fun addNewUser(communityId: String, user: UserRequest): String {
        return userApi.addUser(communityId, user).name
    }

    suspend fun updateUser(communityId: String, user: UserRequest): String {
        return userApi.updateUser(communityId, user.id, user).name
    }
}