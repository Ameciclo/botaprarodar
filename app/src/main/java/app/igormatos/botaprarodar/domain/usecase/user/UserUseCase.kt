package app.igormatos.botaprarodar.domain.usecase.user

import app.igormatos.botaprarodar.data.model.ImageUploadResponse
import app.igormatos.botaprarodar.data.repository.FirebaseHelperRepository
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.converter.user.UserRequestConvert
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.SimpleResult

class UserUseCase(
    private val userRepository: UserRepository,
    private val firebaseHelperRepository: FirebaseHelperRepository,
    private val user: UserRequestConvert
) {

    suspend fun addUser(communityId: String, user: User): SimpleResult<String> {
        val imageResponse = uploadImage(user)
        return saveUser(imageResponse, user, communityId) { _, _ ->
            registerUser(user, communityId)
        }
    }

    private suspend fun saveUser(
        imageResponse: SimpleResult<ImageUploadResponse>?,
        user: User,
        communityId: String,
        actionFunction: suspend (User, String) -> SimpleResult<String>
    ): SimpleResult<String> {
        return when (imageResponse) {
            is SimpleResult.Success -> {
                setupUser(user, imageResponse.data)
                actionFunction(user, communityId)
            }
            is SimpleResult.Error -> {
                SimpleResult.Error(imageResponse.exception)
            }
            else -> {
                SimpleResult.Error(Exception(""))
            }
        }
    }

    private fun setupUser(user: User, imageResponse: ImageUploadResponse) {
        user.profile_picture = imageResponse.fullImagePath
        user.profile_picture_thumbnail = imageResponse.thumbPath
    }

    private suspend fun registerUser(
        user: User,
        communityId: String
    ): SimpleResult<String> {
        return try {
            val userRequest = this.user.convert(user)
            val result = userRepository.addNewUser(communityId, userRequest)
            SimpleResult.Success(result)
        } catch (exception: Exception) {
            SimpleResult.Error(exception)
        }
    }

    private suspend fun uploadImage(user: User) =
        firebaseHelperRepository.uploadImage(
            user.path,
            "community/user/${user.doc_number}"
        )

}