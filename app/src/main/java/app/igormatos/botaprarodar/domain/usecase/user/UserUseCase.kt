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
    private val userConverter: UserRequestConvert
) {

    private var profileSimpleResult: SimpleResult<ImageUploadResponse>? = null
    private var documentFrontSimpleResult: SimpleResult<ImageUploadResponse>? = null
    private var documentBackSimpleResult: SimpleResult<ImageUploadResponse>? = null
    private var residenceSimpleResult: SimpleResult<ImageUploadResponse>? = null

    suspend fun addUser(communityId: String, user: User): SimpleResult<String> {
        uploadImage(user)
        if (checkAllImagesSuccess().not()) return SimpleResult.Error(Exception(""))
        return saveUser(user, communityId) { _, _ ->
            registerUser(user, communityId)
        }
    }

    private suspend fun saveUser(
        user: User,
        communityId: String,
        actionFunction: suspend (User, String) -> SimpleResult<String>
    ): SimpleResult<String> {
        setUserProfileImage(user, (profileSimpleResult as SimpleResult.Success).data)
        setUserDocumentFrontImage(user, (documentFrontSimpleResult as SimpleResult.Success).data)
        setUserDocumentBackImage(user, (documentBackSimpleResult as SimpleResult.Success).data)
        setUserResidenceImage(user, (residenceSimpleResult as SimpleResult.Success).data)
        return actionFunction(user, communityId)
    }

    private suspend fun registerUser(
        user: User,
        communityId: String
    ): SimpleResult<String> {
        return try {
            val userRequest = this.userConverter.convert(user)
            val result = userRepository.addNewUser(communityId, userRequest)
            SimpleResult.Success(result)
        } catch (exception: Exception) {
            SimpleResult.Error(exception)
        }
    }

    private fun checkAllImagesSuccess(): Boolean {
        return (profileSimpleResult != null && profileSimpleResult is SimpleResult.Success
                && documentFrontSimpleResult != null && documentFrontSimpleResult is SimpleResult.Success
                && documentBackSimpleResult != null && documentBackSimpleResult is SimpleResult.Success
                && residenceSimpleResult != null && residenceSimpleResult is SimpleResult.Success)
    }

    private suspend fun uploadImage(user: User) {
        updateProfileImage(user)
        updateDocumentFrontImage(user)
        updateDocumentBackImage(user)
        updateResidenceImage(user)
        return
    }

    private suspend fun updateProfileImage(user: User) {
        if (profileSimpleResult == null || profileSimpleResult is SimpleResult.Error) {
            user.profile_picture?.let {
                profileSimpleResult = firebaseHelperRepository.uploadImage(
                    it,
                    "community/user/${user.doc_number}"
                )
            }
        }
    }

    private suspend fun updateDocumentFrontImage(user: User) {
        if (documentFrontSimpleResult == null || documentFrontSimpleResult is SimpleResult.Error) {
            user.profile_picture?.let {
                documentFrontSimpleResult = firebaseHelperRepository.uploadImage(
                    it,
                    "community/user/${user.doc_number}"
                )
            }
        }
    }

    private suspend fun updateDocumentBackImage(user: User) {
        if (documentBackSimpleResult == null || documentBackSimpleResult is SimpleResult.Error) {
            user.profile_picture?.let {
                documentBackSimpleResult = firebaseHelperRepository.uploadImage(
                    it,
                    "community/user/${user.doc_number}"
                )
            }
        }
    }

    private suspend fun updateResidenceImage(user: User) {
        if (residenceSimpleResult == null || residenceSimpleResult is SimpleResult.Error) {
            user.profile_picture?.let {
                residenceSimpleResult = firebaseHelperRepository.uploadImage(
                    it,
                    "community/user/${user.doc_number}"
                )
            }
        }
    }

    private fun setUserProfileImage(user: User, imageResponse: ImageUploadResponse) {
        user.profile_picture = imageResponse.fullImagePath
        user.profile_picture_thumbnail = imageResponse.thumbPath
    }

    private fun setUserResidenceImage(user: User, imageResponse: ImageUploadResponse) {
        user.residence_proof_picture = imageResponse.fullImagePath
    }

    private fun setUserDocumentBackImage(user: User, imageResponse: ImageUploadResponse) {
        user.doc_picture = imageResponse.fullImagePath
    }

    private fun setUserDocumentFrontImage(user: User, imageResponse: ImageUploadResponse) {
        user.doc_picture_back = imageResponse.fullImagePath
    }
}