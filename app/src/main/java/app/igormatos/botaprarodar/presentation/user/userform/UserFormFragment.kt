package app.igormatos.botaprarodar.presentation.user.userform

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.FragmentUserFormBinding
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.extensions.gone
import com.brunotmgomes.ui.extensions.takePictureIntent
import com.brunotmgomes.ui.extensions.visible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jetbrains.anko.image
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UserFormFragment : Fragment() {

    private val args: UserFormFragmentArgs by navArgs()

    private lateinit var userFormViewModel: UserFormViewModel
    private var mCurrentPhotoPath = ""
    private var currentPhotoId = 0
    private var selectedRacial = 0
    private var racialValues = arrayOf<String>()

    private lateinit var binding: FragmentUserFormBinding

    private val navController: NavController by lazy {
        findNavController()
    }

    companion object {
        private const val REQUEST_PROFILE_PHOTO = 1
        private const val REQUEST_ID_PHOTO = 2
        private const val REQUEST_RESIDENCE_PHOTO = 3
        private const val REQUEST_ID_PHOTO_BACK = 4
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

    private fun setupViewModel(communityUsers: ArrayList<User>): UserFormViewModel {
        userFormViewModel = getViewModel {
            parametersOf(communityUsers)
        }
        return userFormViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        racialValues = requireContext().resources.getStringArray(R.array.racial_options)

        setupListeners()
        setupViewModelStatus()
        checkEditMode()
    }

    private fun checkEditMode() {
        if (args.user != null) {
            setValuesToEditUser(args.user)
            setImageDescriptionsToGone()
            setImageEditDescriptionsToVisible()
        }
    }

    private fun setValuesToEditUser(user: User?) {
        user?.let { userFormViewModel.updateUserValues(it) }
    }

    private fun setImageDescriptionsToGone() {
        if (userHasResidenceProofPicture())
            binding.tvAddResidencePhoto.gone()

        binding.tvAddBackDocumentPhoto.gone()
        binding.tvAddFrontDocumentPhoto.gone()
        binding.tvAddProfilePhoto.gone()
    }

    private fun setImageEditDescriptionsToVisible() {
        if (userHasResidenceProofPicture())
            binding.ivEditResidencePhoto.visible()

        binding.ivEditBackPhoto.visible()
        binding.ivEditFrontPhoto.visible()
        binding.ivEditProfilePhoto.visible()
    }

    private fun userHasResidenceProofPicture(): Boolean {
        return args.user?.residenceProofPicture?.isNotEmpty() == true
    }

    private fun setupViewModelStatus() {
        userFormViewModel.openQuiz.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { data ->
                val (user, editMode) = data
                val direction =
                    UserFormFragmentDirections.actionUserFormFragmentToUserQuizFragment(
                        user,
                        editMode
                    )
                navController.navigate(direction)
            }
        })
    }

    private fun updateViewModelLiveData(whichImageCode: Int, path: String) {
        when (whichImageCode) {
            REQUEST_PROFILE_PHOTO -> {
                binding.viewModel?.setProfileImage(path)
                binding.tvAddProfilePhoto.gone()
                binding.ivEditProfilePhoto.visible()
            }
            REQUEST_ID_PHOTO -> {
                binding.viewModel?.setDocumentImageFront(path)
                binding.tvAddFrontDocumentPhoto.gone()
                binding.ivEditFrontPhoto.visible()
            }
            REQUEST_ID_PHOTO_BACK -> {
                binding.viewModel?.setDocumentImageBack(path)
                binding.tvAddBackDocumentPhoto.gone()
                binding.ivEditBackPhoto.visible()
            }
            REQUEST_RESIDENCE_PHOTO -> {
                binding.viewModel?.setResidenceImage(path)
                binding.tvAddResidencePhoto.gone()
                binding.ivEditResidencePhoto.visible()
            }
        }
    }

    private fun dispatchTakePictureIntent(code: Int) {
        requireActivity().takePictureIntent(code) { path ->
            mCurrentPhotoPath = path
        }
    }

    private fun openDialogToSelectRace() {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle(getString(R.string.add_user_racial))
        dialogBuilder.setSingleChoiceItems(racialValues, selectedRacial) { _, which ->
            selectedRacial = which
        }
        dialogBuilder.setPositiveButton(getString(R.string.ok)) { _, _ ->
            binding.viewModel?.setUserRace(racialValues[selectedRacial])
        }
        dialogBuilder.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            updateViewModelLiveData(requestCode, mCurrentPhotoPath)
        }
    }

    private fun showTipDialog(
        image: Int,
        title: String,
        subtitle: String,
        click: (Boolean) -> Unit
    ) {
        val tipLayout = layoutInflater.inflate(R.layout.dialog_tip, null)

        tipLayout.findViewById<ImageView>(R.id.tipImage).image =
            ContextCompat.getDrawable(requireContext(), image)
        tipLayout.findViewById<TextView>(R.id.tipTitle).text = title
        tipLayout.findViewById<TextView>(R.id.tipSubtitle).text = subtitle

        MaterialAlertDialogBuilder(requireContext())
            .setView(tipLayout)
            .setPositiveButton(getString(R.string.camera_dialog_positive_button_text)) { _, _ ->
                click(true)
            }
            .show()

    }

    private fun setupListeners() {
        binding.profileImageView.setOnClickListener {
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

        binding.ivFrontDocument.setOnClickListener {
            showTipDialog(
                R.drawable.id_front,
                getString(R.string.warning),
                getString(R.string.id_picture_tip)
            ) {
                if (it) {
                    dispatchTakePictureIntent(REQUEST_ID_PHOTO)
                }
            }
        }

        binding.ivBackDocument.setOnClickListener {
            showTipDialog(
                R.drawable.id_back,
                getString(R.string.warning),
                getString(R.string.id_picture_tip)
            ) {
                if (it) {
                    dispatchTakePictureIntent(REQUEST_ID_PHOTO_BACK)
                }
            }
        }

        binding.ivResidenceProof.setOnClickListener {
            dispatchTakePictureIntent(REQUEST_RESIDENCE_PHOTO)
        }

        binding.etRacial.setOnClickListener {
            openDialogToSelectRace()
        }
    }
}