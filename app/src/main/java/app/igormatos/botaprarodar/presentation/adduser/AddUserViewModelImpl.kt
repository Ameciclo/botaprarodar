package app.igormatos.botaprarodar.presentation.adduser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.network.FirebaseHelperModule
import app.igormatos.botaprarodar.domain.model.User

class AddUserViewModelImpl(
    val firebaseHelperModule: FirebaseHelperModule
) : AddUserViewModel() {

    private var userToSend = User()

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
                    userToSend.profile_picture ?: ""
                }
                RequestPictureMode.REQUEST_RESIDENCE_PHOTO -> {
                    userToSend.residence_proof_picture ?: ""
                }
                RequestPictureMode.REQUEST_ID_PHOTO -> {
                    userToSend.doc_picture ?: ""
                }
                else -> {
                    userToSend.doc_picture_back ?: ""
                }
            }
        )
    }

    override fun saveUser() {
        _viewState.value = AddUserViewState.ShowLoading
        isEnable.value = false

        val nameField = name.value
        val addressField = address.value
        val identifierField = identifier.value

        if (nameField.isNullOrEmpty() || addressField.isNullOrEmpty() || identifierField.isNullOrEmpty()) {
            _viewState.value = AddUserViewState.ShowToast(R.string.empties_fields_error)
            _viewState.value = AddUserViewState.HideLoading
            return
        }

        userToSend.apply {
            address = addressField
            name = nameField
            doc_number = identifierField.toLong()
        }

        userToSend.saveRemote { success ->
            if (success) {
                _viewState.value = AddUserViewState.ShowToast(R.string.operation_success_message)
                _viewState.value = AddUserViewState.Finish
            } else {
                _viewState.value = AddUserViewState.HideLoading
                _viewState.value = AddUserViewState.ShowToast(R.string.something_happened_error)
                isEnable.value = true
            }
        }
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
                        userToSend.profile_picture = imagePath
                        userToSend.profile_picture_thumbnail = thumbPath
                        isProfileImageLoad.value = false
                    }
                    RequestPictureMode.REQUEST_RESIDENCE_PHOTO -> {
                        userToSend.residence_proof_picture = imagePath
                        isAddressImageLoad.value = false
                    }
                    RequestPictureMode.REQUEST_ID_PHOTO -> {
                        userToSend.doc_picture = imagePath
                        isDocImageLoad.value = false
                    }
                    RequestPictureMode.REQUEST_ID_PHOTO_BACK -> {
                        userToSend.doc_picture_back = imagePath
                        isDocBackImageLoad.value = false
                    }
                }
            } else {
                _viewState.value = AddUserViewState.ShowToast(R.string.something_happened_error)
            }
        }
    }
}