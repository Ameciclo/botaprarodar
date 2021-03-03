package app.igormatos.botaprarodar.domain.usecase.userForm

import app.igormatos.botaprarodar.data.model.ImageUploadResponse
import app.igormatos.botaprarodar.data.repository.FirebaseHelperRepository
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.converter.user.UserRequestConvert
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.SimpleResult

private const val FIREBASE_URL = "https://"

class UserFormUseCase(
    private val userRepository: UserRepository,
    private val firebaseHelperRepository: FirebaseHelperRepository,
    private val userConverter: UserRequestConvert
) {

    private var profileSimpleResult: SimpleResult<ImageUploadResponse>? = null
    private var documentFrontSimpleResult: SimpleResult<ImageUploadResponse>? = null
    private var documentBackSimpleResult: SimpleResult<ImageUploadResponse>? = null
    private var residenceSimpleResult: SimpleResult<ImageUploadResponse>? = null

    suspend fun addUser(communityId: String, user: User): SimpleResult<String> {
        uploadImages(user)
        if (!checkAllImagesSuccess()) {
            return SimpleResult.Error(Exception(""))
        }
        return saveUser(user, communityId) { _, _ ->
            registerUser(user, communityId)
        }
    }

    suspend fun updateUser(communityId: String, user: User): SimpleResult<String> {
        uploadImages(user)
        if (!checkAllImagesSuccess()) {
            return SimpleResult.Error(Exception(""))
        }
        return saveUser(user, communityId) { _, _ ->
            updateUser(user, communityId)
        }
    }

    private suspend fun saveUser(
        user: User,
        communityId: String,
        actionFunction: suspend (User, String) -> SimpleResult<String>
    ): SimpleResult<String> {
        setUserImages(user)
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

    private suspend fun updateUser(
        user: User,
        communityId: String
    ): SimpleResult<String> {
        return try {
            val userRequest = this.userConverter.convert(user)
            val result = userRepository.updateUser(communityId, userRequest)
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

    private suspend fun uploadImages(user: User) {
        checkFirebaseUrl(user.profile_picture, user, profileSimpleResult) {
            uploadProfileImage(user)
        }
        checkFirebaseUrl(user.doc_picture, user, documentFrontSimpleResult) {
            uploadOnlyImages(user.doc_picture, user.doc_number, documentFrontSimpleResult)
        }
        checkFirebaseUrl(user.doc_picture_back, user, documentBackSimpleResult) {
            uploadOnlyImages(user.doc_picture_back, user.doc_number, documentBackSimpleResult)
        }
        checkFirebaseUrl(user.residence_proof_picture, user, residenceSimpleResult) {
            uploadOnlyImages(user.residence_proof_picture, user.doc_number, residenceSimpleResult)
        }
    }

    private suspend fun uploadProfileImage(user: User) {
        if (profileSimpleResult == null || profileSimpleResult is SimpleResult.Error) {
            user.profile_picture?.let { path ->
                firebaseHelperRepository.uploadImageAndThumb(
                    path,
                    "community/user/${user.doc_number}"
                ).also { profileSimpleResult = it }
            }
        }
    }

    private suspend fun uploadOnlyImages(
        pathImage: String?,
        docNumber: Long,
        simpleResult: SimpleResult<ImageUploadResponse>?
    ) {
        if (simpleResult == null || simpleResult is SimpleResult.Error) {
            pathImage?.let { path ->
                firebaseHelperRepository.uploadOnlyImage(
                    path,
                    "community/user/$docNumber"
                ).also { updateSimpleResult(it, simpleResult) }
            }
        }
    }

    private fun updateSimpleResult(
        simpleResult: SimpleResult<ImageUploadResponse>?,
        simpleResultToUpdate: SimpleResult<ImageUploadResponse>?
    ) {
        when (simpleResultToUpdate) {
            profileSimpleResult -> profileSimpleResult = simpleResult
            documentFrontSimpleResult -> documentFrontSimpleResult = simpleResult
            documentBackSimpleResult -> documentBackSimpleResult = simpleResult
            residenceSimpleResult -> residenceSimpleResult = simpleResult
            else -> {
            }
        }
    }

    private fun setUserImages(user: User) {
        val profile = (profileSimpleResult as SimpleResult.Success).data
        val docFront = (documentFrontSimpleResult as SimpleResult.Success).data
        val docBack = (documentBackSimpleResult as SimpleResult.Success).data
        val residence = (residenceSimpleResult as SimpleResult.Success).data

        user.profile_picture = profile.fullImagePath
        user.profile_picture_thumbnail = profile.thumbPath
        user.residence_proof_picture = residence.fullImagePath
        user.doc_picture = docFront.fullImagePath
        user.doc_picture_back = docBack.fullImagePath
    }

    private suspend fun checkFirebaseUrl(
        pathImage: String?,
        user: User,
        simpleResult: SimpleResult<ImageUploadResponse>?,
        actionFunction: suspend () -> Unit
    ) {
        if (pathImage?.contains(FIREBASE_URL) == true) {
            updateSimpleResult(user, simpleResult)
        } else {
            actionFunction()
        }
    }

    private fun getSimpleResultSuccess(
        fullImagePath: String?,
        thumbImagePath: String? = ""
    ): SimpleResult.Success<ImageUploadResponse> {
        return SimpleResult.Success(ImageUploadResponse(fullImagePath, thumbImagePath))
    }

    private fun updateSimpleResult(
        user: User,
        simpleResultToUpdate: SimpleResult<ImageUploadResponse>?
    ) {
        when (simpleResultToUpdate) {
            profileSimpleResult -> profileSimpleResult = getSimpleResultSuccess(
                user.profile_picture,
                user.profile_picture_thumbnail
            )
            documentFrontSimpleResult ->
                documentFrontSimpleResult = getSimpleResultSuccess(user.doc_picture)
            documentBackSimpleResult ->
                documentBackSimpleResult = getSimpleResultSuccess(user.doc_picture_back)
            residenceSimpleResult ->
                residenceSimpleResult = getSimpleResultSuccess(user.residence_proof_picture)
            else -> {
            }
        }
    }
}