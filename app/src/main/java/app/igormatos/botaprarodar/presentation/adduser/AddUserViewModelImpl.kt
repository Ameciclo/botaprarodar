package app.igormatos.botaprarodar.presentation.adduser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.exception.BlankFieldsException
import app.igormatos.botaprarodar.data.network.FirebaseHelperModule
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.user.UserRequest
import app.igormatos.botaprarodar.domain.usecase.user.AddUserUseCase
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.launch

class AddUserViewModelImpl(
    val firebaseHelperModule: FirebaseHelperModule,
    val userUseCase: AddUserUseCase
) : AddUserViewModel() {

    private var userRequest = UserRequest()
    private var code: Int = RequestPictureMode.REQUEST_NOTHING

    override val name = MutableLiveData("")
    override val address = MutableLiveData("")
    override val identifier = MutableLiveData("")
    override val gender = MutableLiveData(3)

    override val profilePhoto = MutableLiveData("")
    override val residencePhoto = MutableLiveData("")
    override val docPhoto = MutableLiveData("")
    override val docBackPhoto = MutableLiveData("")

    override val isAddressImageLoad = MutableLiveData(false)
    override val isDocBackImageLoad = MutableLiveData(false)
    override val isDocImageLoad = MutableLiveData(false)
    override val isProfileImageLoad = MutableLiveData(false)

    override val isEditing = MutableLiveData(false)
    override val isEnable = MutableLiveData(true)

    private val _viewState = MutableLiveData<AddUserViewState>()
    override val viewState: LiveData<AddUserViewState>
        get() = _viewState

    override fun setupUserForEdit(user: User?) {
        if (user != null) {
            isEditing.value = true
            name.value = user.name
            address.value = user.address
            identifier.value = "${user.doc_number}"
            gender.value = user.gender

            profilePhoto.value = user.profile_picture
            residencePhoto.value = user.residence_proof_picture
            docPhoto.value = user.doc_picture
            docBackPhoto.value = user.doc_picture_back

            userRequest.id = user.id
            userRequest.profilePicture = user.profile_picture
            userRequest.residenceProofPicture = user.residence_proof_picture
            userRequest.docPicture = user.doc_picture
            userRequest.docPictureBack = user.doc_picture_back
        }
    }

    override fun handleClickInImage(isEditing: Boolean, hasDialog: Boolean, code: Int) {
        this.code = code

        if (isEditing) {
            displayImageInFullScream(code)
        } else {
            takePicture(hasDialog, code)
        }
    }

    private fun takePicture(hasDialog: Boolean, code: Int) {
        if (hasDialog) {
            setDialogViewState(code)
        } else {
            callIntentTakePicture()
        }
    }

    private fun displayImageInFullScream(code: Int) {
        _viewState.value = AddUserViewState.ImageFullScream(
            when (code) {
                RequestPictureMode.REQUEST_PROFILE_PHOTO -> {
                    userRequest.profilePicture ?: ""
                }
                RequestPictureMode.REQUEST_RESIDENCE_PHOTO -> {
                    userRequest.residenceProofPicture ?: ""
                }
                RequestPictureMode.REQUEST_ID_PHOTO -> {
                    userRequest.docPicture ?: ""
                }
                else -> {
                    userRequest.docPictureBack ?: ""
                }
            }
        )
    }

    override fun saveUser() {
        viewModelScope.launch {
            _viewState.value = AddUserViewState.ShowLoading
            isEnable.value = false

            userRequest.name = name.value
            userRequest.address = address.value
            userRequest.docNumber =
                if (identifier.value.isNullOrBlank())
                    0L
                else
                    identifier.value?.toLong() ?: 0L

            val result = if (isEditing.value!!)
                userUseCase.updateUser(userRequest, userRequest.id)
            else
                userUseCase.saveUser(userRequest)

            when (result) {
                is SimpleResult.Success -> successStateHandler()
                is SimpleResult.Error -> errorStateHandler(result.exception)
            }
        }
    }

    private fun errorStateHandler(exception: Exception) {
        isEnable.value = true
        _viewState.value = AddUserViewState.HideLoading
        _viewState.value = AddUserViewState.ShowToast(
            if (exception is BlankFieldsException) {
                R.string.empties_fields_error
            } else {
                R.string.something_happened_error
            }
        )
    }

    private fun successStateHandler() {
        _viewState.value = AddUserViewState.ShowToast(R.string.operation_success_message)
        _viewState.value = AddUserViewState.Finish
    }

    override fun setGender(genderInput: Int) {
        gender.value = genderInput
    }

    private fun setDialogViewState(code: Int) {
        _viewState.value = when (code) {
            RequestPictureMode.REQUEST_PROFILE_PHOTO -> {
                AddUserViewState.ShowDialog(
                    R.string.profile_picture,
                    R.string.profile_picture_tip,
                    R.drawable.iconfinder_user_profile_imagee
                )
            }
            RequestPictureMode.REQUEST_ID_PHOTO -> {
                AddUserViewState.ShowDialog(
                    R.string.warning,
                    R.string.id_picture_tip,
                    R.drawable.id_front
                )
            }
            RequestPictureMode.REQUEST_ID_PHOTO_BACK -> {
                AddUserViewState.ShowDialog(
                    R.string.warning,
                    R.string.id_picture_tip,
                    R.drawable.id_back
                )
            }
            else -> null
        }
    }

    override fun callIntentTakePicture() {
        _viewState.value = AddUserViewState.TakePicture(this.code)
    }

    override fun updatePhoto(photoWrapper: AddUserWrapper) {
        if (photoWrapper.photoPath.isNullOrEmpty()) return
        updateImageView(photoWrapper.requestCode, photoWrapper.photoPath)
        uploadToFirebase(photoWrapper.requestCode, photoWrapper.photoPath)
    }

    private fun updateImageView(code: Int?, path: String) {
        when (code) {
            RequestPictureMode.REQUEST_PROFILE_PHOTO -> {
                profilePhoto.value = path
                isProfileImageLoad.value = true
            }
            RequestPictureMode.REQUEST_RESIDENCE_PHOTO -> {
                residencePhoto.value = path
                isAddressImageLoad.value = true
            }
            RequestPictureMode.REQUEST_ID_PHOTO -> {
                docPhoto.value = path
                isDocImageLoad.value = true
            }
            RequestPictureMode.REQUEST_ID_PHOTO_BACK -> {
                docBackPhoto.value = path
                isDocBackImageLoad.value = true
            }
        }
    }

    private fun uploadToFirebase(code: Int?, path: String) {
        firebaseHelperModule.uploadImage(path) { success, imagePath, thumbPath ->
            if (success) {
                when (code) {
                    RequestPictureMode.REQUEST_PROFILE_PHOTO -> {
                        userRequest.profilePicture = imagePath
                        userRequest.profilePictureThumbnail = thumbPath
                        isProfileImageLoad.value = false
                    }
                    RequestPictureMode.REQUEST_RESIDENCE_PHOTO -> {
                        userRequest.residenceProofPicture = imagePath
                        isAddressImageLoad.value = false
                    }
                    RequestPictureMode.REQUEST_ID_PHOTO -> {
                        userRequest.docPicture = imagePath
                        isDocImageLoad.value = false
                    }
                    RequestPictureMode.REQUEST_ID_PHOTO_BACK -> {
                        userRequest.docPictureBack = imagePath
                        isDocBackImageLoad.value = false
                    }
                }
            } else {
                _viewState.value = AddUserViewState.ShowToast(R.string.something_happened_error)
            }
        }
    }
}