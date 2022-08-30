package app.igormatos.botaprarodar.domain.usecase.users

import app.igormatos.botaprarodar.common.enumType.UserSortFields
import app.igormatos.botaprarodar.common.extensions.onlyAvailableUsers
import app.igormatos.botaprarodar.common.extensions.sortBy
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class UsersUseCase(private val userRepository: UserRepository) {

    suspend fun getAvailableUsersByCommunityId(communityId: String): SimpleResult<List<User>> {
        return when(val result = userRepository.getUsersByCommunityId(communityId)) {
            is SimpleResult.Success -> {
                SimpleResult.Success(
                    result.data.values.toList()
                        .onlyAvailableUsers()
                        .sortBy(UserSortFields.NAME)
                )
            }
            is SimpleResult.Error -> {
                SimpleResult.Error(result.exception)
            }
        }
    }
}
