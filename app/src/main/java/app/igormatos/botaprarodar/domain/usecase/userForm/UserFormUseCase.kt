package app.igormatos.botaprarodar.domain.usecase.userForm

import app.igormatos.botaprarodar.data.model.ImageUploadResponse
import app.igormatos.botaprarodar.data.repository.FirebaseHelperRepository
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.converter.user.UserRequestConvert
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.SimpleResult
import java.io.File

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

    suspend fun addUser(user: User): SimpleResult<AddDataResponse> {
        uploadImages(user)
        if (!checkAllImagesSuccess()) {
            return SimpleResult.Error(Exception(""))
        }
        return saveUser(user) {
            registerUser(it)
        }
    }

    suspend fun startUpdateUser(
        user: User,
        deleteImagesPath: List<String> = emptyList()
    ): SimpleResult<AddDataResponse> {
        uploadImages(user)
        if (!checkAllImagesSuccess()) {
            return SimpleResult.Error(Exception(""))
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

    suspend fun deleteImages(imagePathsToDelete: List<String>){
        for (path in imagePathsToDelete) {
            if (path.contains(FIREBASE_URL)) {
                deleteImageFromRepository(path)
            } else {
                deleteImageLocal(path)
            }
        }
    }

    suspend fun deleteImageFromRepository(path: String): SimpleResult<Unit>{
        return firebaseHelperRepository.deleteImageResource(path)
    }

    fun deleteImageLocal(path: String): SimpleResult<Unit> {
        var isFileDeleted = false
        if (File(path).isFile) {
            isFileDeleted = File(path).delete()
        }
        return if (isFileDeleted){
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
        return (profileSimpleResult != null && profileSimpleResult is SimpleResult.Success
                && documentFrontSimpleResult != null && documentFrontSimpleResult is SimpleResult.Success
                && documentBackSimpleResult != null && documentBackSimpleResult is SimpleResult.Success
                && residenceSimpleResult != null && residenceSimpleResult is SimpleResult.Success)
    }

    private suspend fun uploadImages(user: User) {
        checkFirebaseUrl(user.profilePicture, user, profileSimpleResult) {
            uploadProfileImage(user)
        }
        checkFirebaseUrl(user.docPicture, user, documentFrontSimpleResult) {
            uploadOnlyImages(user.docPicture, user.docNumber, documentFrontSimpleResult)
        }
        checkFirebaseUrl(user.docPictureBack, user, documentBackSimpleResult) {
            uploadOnlyImages(user.docPictureBack, user.docNumber, documentBackSimpleResult)
        }
        checkFirebaseUrl(user.residenceProofPicture, user, residenceSimpleResult) {
            if (user.residenceProofPicture.isNullOrEmpty()) {
                residenceSimpleResult = SimpleResult.Success(ImageUploadResponse("", ""))
            } else {
                uploadOnlyImages(user.residenceProofPicture, user.docNumber, residenceSimpleResult)
            }
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

        user.profilePicture = profile.fullImagePath
        user.profilePictureThumbnail = profile.thumbPath
        user.residenceProofPicture = residence.fullImagePath
        user.docPicture = docFront.fullImagePath
        user.docPictureBack = docBack.fullImagePath
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
            documentFrontSimpleResult ->
                documentFrontSimpleResult = getSimpleResultSuccess(user.docPicture)
            documentBackSimpleResult ->
                documentBackSimpleResult = getSimpleResultSuccess(user.docPictureBack)
            residenceSimpleResult ->
                residenceSimpleResult = getSimpleResultSuccess(user.residenceProofPicture)
            else -> {
            }
        }
    }
}