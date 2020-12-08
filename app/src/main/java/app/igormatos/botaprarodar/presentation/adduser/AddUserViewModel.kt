package app.igormatos.botaprarodar.presentation.adduser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.domain.model.User

abstract class AddUserViewModel : ViewModel() {
    abstract val viewState: LiveData<AddUserViewState>

    abstract val name: MutableLiveData<String>
    abstract val address: MutableLiveData<String>
    abstract val identifier: MutableLiveData<String>
    abstract val gender: MutableLiveData<Int>

    abstract val profilePhoto: MutableLiveData<String>
    abstract val residencePhoto: MutableLiveData<String>
    abstract val docPhoto: MutableLiveData<String>
    abstract val docBackPhoto: MutableLiveData<String>

    abstract val isEditing: MutableLiveData<Boolean>
    abstract val isEnable: MutableLiveData<Boolean>

    abstract val isProfileImageLoad: MutableLiveData<Boolean>
    abstract val isAddressImageLoad: MutableLiveData<Boolean>
    abstract val isDocImageLoad: MutableLiveData<Boolean>
    abstract val isDocBackImageLoad: MutableLiveData<Boolean>

    abstract fun setupUserForEdit(user: User?)
    abstract fun handleClickInImage(isEditing: Boolean, hasDialog: Boolean, code: Int)
    abstract fun callIntentTakePicture()
    abstract fun saveUser()
    abstract fun setGender(gender: Int)
    abstract fun updatePhoto(photoWrapper: AddUserWrapper)
}