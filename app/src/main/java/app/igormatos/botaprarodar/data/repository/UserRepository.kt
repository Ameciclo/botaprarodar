package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.common.utils.formatAsJSONValidType
import app.igormatos.botaprarodar.data.network.api.UserApi
import app.igormatos.botaprarodar.data.network.safeApiCall
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class UserRepository(private val userApi: UserApi) {

    suspend fun addNewUser(user: User): SimpleResult<AddDataResponse> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                userApi.addUser(user)
            }
        }
    }

    suspend fun updateUser(user: User): SimpleResult<AddDataResponse> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                userApi.updateUser(user.id.orEmpty(), user)
            }
        }
    }

    suspend fun getUsersByCommunityId(communityId: String): SimpleResult<Map<String, User>> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                userApi.getUsersByCommunityId(formatAsJSONValidType(communityId))
            }
        }
    }


}