package app.igormatos.botaprarodar.domain.usecase.users

import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ValidateUserWithdraw(private val bikeRepository: BikeRepository,
                           private val userRepository: UserRepository) {
    suspend fun execute(user: User): Boolean {
        if (hasWithdrawActiveAndUserId(user)) {
            if (isInvalidActiveWithdraw(user.id!!)) {
                updateUserWithoutActiveWithdraw(user)
            }
        }
        return user.hasActiveWithdraw
    }

    private suspend fun updateUserWithoutActiveWithdraw(user: User) {
        user.hasActiveWithdraw = false
        userRepository.updateUser(user)
    }

    private suspend fun isInvalidActiveWithdraw(userId: String): Boolean {
        val response = bikeRepository.getBikeWithWithdrawByUser(userId)

        if (response is SimpleResult.Success) {
            return response.data.isEmpty()
        }
        return false
    }

    private fun hasWithdrawActiveAndUserId(user: User): Boolean {
        return user.hasActiveWithdraw && user.id != null
    }
}