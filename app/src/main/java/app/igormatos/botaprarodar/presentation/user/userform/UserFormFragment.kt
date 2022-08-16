package app.igormatos.botaprarodar.presentation.user.userform

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.DialogTipBinding
import app.igormatos.botaprarodar.databinding.FragmentUserFormBinding
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.user.UserActivity
import com.brunotmgomes.ui.extensions.takePictureIntent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import kotlinx.coroutines.launch
import org.jetbrains.anko.image
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.util.*

class UserFormFragment : Fragment() {

    private val args: UserFormFragmentArgs by navArgs()

    private lateinit var userFormViewModel: UserFormViewModel
    private var mCurrentPhotoPath = ""
    private var currentPhotoId = 0

    private lateinit var binding: FragmentUserFormBinding

    private val navController: NavController by lazy {
        findNavController()
    }

    companion object {
        private const val REQUEST_PROFILE_PHOTO = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val communityUsers = getCommunityUsers()
        setupViewModel(communityUsers)
        binding = FragmentUserFormBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = userFormViewModel
        return binding.root
    }

    private fun getCommunityUsers(): ArrayList<User> {
        val communityUsers = args.communityUsers?.toCollection(ArrayList())
        if (communityUsers.isNullOrEmpty()) {
            return arrayListOf()
        }
        return communityUsers
    }

    private fun setupViewModel(
        communityUsers: ArrayList<User>,
    ): UserFormViewModel {
        userFormViewModel = getViewModel {
            parametersOf(communityUsers)
        }
        return userFormViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        navigateToSocialDataObserver()
        setupListenerCancelButton()
        checkEditMode()
    }

    private fun checkEditMode() {
        if (args.user != null) {
            setValuesToEditUser(args.user)
        }
    }

    private fun setValuesToEditUser(user: User?) {
        user?.let {
            userFormViewModel.updateUserValues(it)
        }
    }

    private fun setupListeners() {
        activity?.let { it ->
            binding.userBirthdayCet.setupClick(it.supportFragmentManager) { date:String ->
                binding.viewModel?.userBirthday?.value = date
            }
        }

        binding.userPhoneCet.addEditTextListener(PhoneNumberFormattingTextWatcher("BR"))

        binding.perfilPictureCpp.setupClick {
            showTipDialog(
                R.drawable.iconfinder_user_profile_imagee,
                getString(R.string.profile_picture),
                getString(R.string.profile_picture_tip)
            ) {
                if (it) {
                    currentPhotoId = REQUEST_PROFILE_PHOTO
                    dispatchTakePictureIntent(REQUEST_PROFILE_PHOTO)
                }
            }
        }
    }

    private fun navigateToSocialDataObserver() {
        userFormViewModel.openUserSocialData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { data ->
                val (user, editMode, deleteImagePaths) = data
                val direction =
                    UserFormFragmentDirections.actionUserFormFragmentToSocialDataFragment(
                        user,
                        editMode,
                        deleteImagePaths.toTypedArray(),
                        getCommunityUsers().toTypedArray(),
                    )
                activity?.let { userActivity ->
                    (userActivity as UserActivity).navigateToNext()
                }
                navController.navigate(direction)
            }
        }
    }

    private fun setupListenerCancelButton() {
        binding.cancelButton.setOnClickListener {
            activity?.finish()
        }
    }

    private fun updateViewModelLiveData(whichImageCode: Int, path: String) {
        if (whichImageCode == REQUEST_PROFILE_PHOTO) {
            binding.viewModel?.setProfileImage(path)
        }
    }

    private fun dispatchTakePictureIntent(code: Int) {
        requireActivity().takePictureIntent(code) { path ->
            mCurrentPhotoPath = path
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            lifecycleScope.launch {
                compressImage(mCurrentPhotoPath)
                updateViewModelLiveData(requestCode, mCurrentPhotoPath)
            }

        }
    }

    private suspend fun compressImage(path: String) {
        val imageFile = File(path)
        Compressor.compress(requireContext(), imageFile) {
            default()
            destination(imageFile)
        }
    }

    private fun showTipDialog(
        image: Int,
        title: String,
        subtitle: String,
        click: (Boolean) -> Unit
    ) {
        val tipDialogBinding = DialogTipBinding.inflate(layoutInflater)

        tipDialogBinding.tipImage.image = ContextCompat.getDrawable(requireContext(), image)
        tipDialogBinding.tipTitle.text = title
        tipDialogBinding.tipSubtitle.text = subtitle

        MaterialAlertDialogBuilder(requireContext())
            .setView(tipDialogBinding.root)
            .setPositiveButton(getString(R.string.camera_dialog_positive_button_text)) { _, _ ->
                click(true)
            }
            .show()
    }
}

