package app.igormatos.botaprarodar.domain.usecase.user

import app.igormatos.botaprarodar.common.exception.BlankFieldsException
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.model.user.UserRequest
import com.brunotmgomes.ui.SimpleResult

class AddUserUseCase(val userRepository: UserRepository) {

    suspend fun saveUser(user: UserRequest): SimpleResult<String> {
        return try {
            if (!hasBlankValues(user)) {
                val userId = userRepository.saveUser(user)
                user.id = userId
                SimpleResult.Success(userRepository.updateUser(user, userId))
            } else {
                SimpleResult.Error(BlankFieldsException("Preencha todos os campos"))
            }
        } catch (exception: Exception) {
            SimpleResult.Error(exception)
        }
    }

    suspend fun updateUser(user: UserRequest, userId: String?): SimpleResult<String> {
        return try {
            if (!hasBlankValues(user))
                SimpleResult.Success(userRepository.updateUser(user, userId))
            else
                SimpleResult.Error(BlankFieldsException("Preencha todos os campos"))
        } catch (exception: Exception) {
            SimpleResult.Error(exception)
        }
    }

    private fun hasBlankValues(user: UserRequest): Boolean {
        return user.name.isNullOrEmpty() ||
                user.address.isNullOrEmpty() ||
                user.docNumber == 0L ||
                user.residenceProofPicture.isNullOrEmpty() ||
                user.profilePicture.isNullOrEmpty() ||
                user.docPicture.isNullOrEmpty() ||
                user.docPictureBack.isNullOrEmpty()
    }
}