package app.igormatos.botaprarodar.domain.usecase.users

import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class GetUserByIdUseCase(private val userRepository: UserRepository) {
    suspend fun execute(userId: String): User? {
        return when (val result = userRepository.getUserBy(userId)) {
            is SimpleResult.Success -> {
                result.data
            }
            else -> null
        }
    }
}