package app.igormatos.botaprarodar.domain.usecase.users

import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class UsersUseCase(private val userRepository: UserRepository) {

    suspend fun getUsers(communityId: String): Flow<SimpleResult<List<User>>> {
        return userRepository.getUsers(communityId)
    }
}