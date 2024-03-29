package app.igormatos.botaprarodar.domain.usecase.userForm

import app.igormatos.botaprarodar.common.enumType.UserMotivationType
import app.igormatos.botaprarodar.data.model.ImageUploadResponse
import app.igormatos.botaprarodar.data.repository.FirebaseHelperRepository
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.usecase.users.UsersUseCase
import com.brunotmgomes.ui.SimpleResult
import com.brunotmgomes.ui.extensions.isNotNullOrNotBlank
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File

private const val FIREBASE_URL = "https://"

class UserFormUseCase(
    private val userRepository: UserRepository,
    private val firebaseHelperRepository: FirebaseHelperRepository
) {

    private var profileSimpleResult: SimpleResult<ImageUploadResponse>? = null

    suspend fun addUser(user: User): SimpleResult<AddDataResponse> {

        if (user.profilePicture.isNotNullOrNotBlank()) {
            uploadImages(user)
            if (!checkAllImagesSuccess()) {
                return SimpleResult.Error(Exception(""))
            }
        }

        return saveUser(user) {
            registerUser(it)
        }
    }

    suspend fun startUpdateUser(
        user: User,
        deleteImagesPath: List<String> = emptyList()
    ): SimpleResult<AddDataResponse> {
        if (user.profilePicture.isNotNullOrNotBlank()) {
            uploadImages(user)
            if (!checkAllImagesSuccess()) {
                return SimpleResult.Error(Exception(""))
            }
        }
        val responseAction = saveUser(user) {
            val response = updateUser(it)
            response.let { res ->
                when (res) {
                    is SimpleResult.Success -> {
                        deleteImages(deleteImagesPath)
                    }
                }
            }
            return@saveUser response
        }
        return responseAction
    }

    private fun deleteImages(imagePathsToDelete: List<String>) {
        for (path in imagePathsToDelete) {
            if (path.contains(FIREBASE_URL)) {
                deleteImageFromRepository(path)
            } else {
                deleteImageLocal(path)
            }
        }
    }

    fun deleteImageFromRepository(path: String): SimpleResult<Unit> {
        return firebaseHelperRepository.deleteImageResource(path)
    }

    fun deleteImageLocal(path: String): SimpleResult<Unit> {
        var isFileDeleted = false
        if (File(path).isFile) {
            isFileDeleted = File(path).delete()
        }
        return if (isFileDeleted) {
            SimpleResult.Success(Unit)
        } else {
            SimpleResult.Error(Exception("File not deleted"))
        }
    }

    private suspend fun saveUser(
        user: User,
        actionFunction: suspend (User) -> SimpleResult<AddDataResponse>
    ): SimpleResult<AddDataResponse> {
        setUserImages(user)
        return actionFunction(user)
    }

    private suspend fun registerUser(user: User): SimpleResult<AddDataResponse> {
        return userRepository.addNewUser(user)
    }

    private suspend fun updateUser(user: User): SimpleResult<AddDataResponse> {
        return userRepository.updateUser(user)
    }

    private fun checkAllImagesSuccess(): Boolean {
        return (profileSimpleResult != null && profileSimpleResult is SimpleResult.Success)
    }

    private suspend fun uploadImages(user: User) {
        checkFirebaseUrl(user.profilePicture, user, profileSimpleResult) {
            uploadProfileImage(user)
        }
    }

    private suspend fun uploadProfileImage(user: User) {
        if (profileSimpleResult == null || profileSimpleResult is SimpleResult.Error) {
            user.profilePicture?.let { path ->
                firebaseHelperRepository.uploadImageAndThumb(
                    path,
                    "community/user/${user.docNumber}"
                ).also { profileSimpleResult = it }
            }
        }
    }

    private fun setUserImages(user: User) {
        if (user.profilePicture.isNotNullOrNotBlank()) {
            val profile = (profileSimpleResult as SimpleResult.Success).data

            user.profilePicture = profile.fullImagePath
            user.profilePictureThumbnail = profile.thumbPath
        }
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
                user.profilePicture,
                user.profilePictureThumbnail
            )
            else -> {
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getUserMotivations(): Map<Int, String> = userRepository.getUserMotivations()

    fun getUserMotivationValue(index: Int?): String {
        return index?.let { UserMotivationType.getUserMotivationTypeByIndex(it)?.value }?:""
    }

    fun getUserMotivationIndex(value: String): Int? {
        return UserMotivationType.getUserMotivationTypeByValue(value)?.index
    }
}
