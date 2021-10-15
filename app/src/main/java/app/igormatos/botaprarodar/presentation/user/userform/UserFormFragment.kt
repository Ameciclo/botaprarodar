package app.igormatos.botaprarodar.presentation.user.userform

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.utils.EditTextFormatMask
import app.igormatos.botaprarodar.common.ViewModelStatus
import app.igormatos.botaprarodar.common.biding.ImageBindingAdapter.setImagePathOrUrl
import app.igormatos.botaprarodar.common.ViewModelStatus
import app.igormatos.botaprarodar.common.biding.ImageBindingAdapter.setImagePathOrUrl
import app.igormatos.botaprarodar.common.biding.ImageBindingAdapter.setImagePathOrUrlWithTransformation
import app.igormatos.botaprarodar.common.utils.EditTextFormatMask
import app.igormatos.botaprarodar.databinding.FragmentUserFormBinding
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.extensions.gone
import com.brunotmgomes.ui.extensions.takePictureIntent
import com.brunotmgomes.ui.extensions.visible
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jetbrains.anko.image
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.collections.ArrayList

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
        val racialOptions = resources.getStringArray(R.array.racial_options).toList()
        val incomeOptions = resources.getStringArray(R.array.income_options).toList()
        val schoolingOptions = resources.getStringArray(R.array.schooling_options).toList()
        val schoolingStatusOptions = resources.getStringArray(R.array.schooling_status_options).toList()
        val genderOptions = resources.getStringArray(R.array.gender_options).toList()
        val mapOptions = mapOf("racialOptions" to racialOptions, "incomeOptions" to incomeOptions,
            "schoolingOptions" to schoolingOptions, "schoolingStatusOptions" to schoolingStatusOptions,
            "genderOptions" to genderOptions )
        setupViewModel(communityUsers, mapOptions)
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

    private fun setupViewModel(communityUsers: ArrayList<User>, mapOptions: Map<String, List<String>>): UserFormViewModel {
        userFormViewModel = getViewModel {
            parametersOf(communityUsers, mapOptions)
        }
        return userFormViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupViewModelObservers()
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
        user?.let {
            userFormViewModel.updateUserValues(it)
            if (it.schoolingStatus.isNullOrBlank()){
                binding.schoolingStatusRadioGroup.clearCheck()
            }
        }
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

    private fun setupViewModelObservers() {
        userFormViewModel.openQuiz.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { data ->
                val (user, editMode, deleteImagePaths) = data
                val direction =
                    UserFormFragmentDirections.actionUserFormFragmentToUserQuizFragment(
                        user,
                        editMode,
                        deleteImagePaths.toTypedArray()
                    )
                navController.navigate(direction)
            }
        })
        userFormViewModel.statusDeleteImage.observe(viewLifecycleOwner, {
            when (it) {
                is ViewModelStatus.Success -> {
                    binding.tvAddResidencePhoto.visible()
                    binding.ivEditResidencePhoto.gone()
                }
                is ViewModelStatus.Error -> {
                    binding.tvAddResidencePhoto.gone()
                    binding.ivEditResidencePhoto.visible()
                }
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

    private fun createDialogGender() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.add_user_gender))
            setSingleChoiceItems(binding.viewModel?.getGenderList()?.toTypedArray(), binding.viewModel?.getSelectedGenderListIndex() ?: 0) { _, which ->
                binding.viewModel?.setSelectGenderIndex(which)
            }
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                binding.viewModel?.confirmUserGender()
            }
            create().show()
        }
    }

    private fun createDialogSchooling() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.add_user_schooling))
            setSingleChoiceItems(binding.viewModel?.getSchoolingList()?.toTypedArray(), binding.viewModel?.getSelectedSchoolingListIndex() ?: 0) { _, which ->
                binding.viewModel?.setSelectSchoolingIndex(which)
            }
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                binding.viewModel?.confirmUserSchooling()
            }
            create().show()
        }
    }

    private fun openDialogToSelectIncome() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.add_user_income))
            setSingleChoiceItems(binding.viewModel?.getIncomeList()?.toTypedArray(), binding.viewModel?.getSelectedIncomeListIndex() ?: 0) { _, which ->
                binding.viewModel?.setSelectIncomeIndex(which)
            }
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                binding.viewModel?.confirmUserIncome()
            }
            create().show()
        }
    }

    private fun openDialogToSelectRace() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.add_user_racial))
            setSingleChoiceItems(binding.viewModel?.getRacialList()?.toTypedArray(), binding.viewModel?.getSelectedRacialListIndex() ?: 0) { _, which ->
                binding.viewModel?.setSelectRacialIndex(which)
            }
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                binding.viewModel?.confirmUserRace()
            }
            create().show()
        }
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

    private fun openDialogChangeImage() {
        val changeImageLayout = layoutInflater.inflate(R.layout.dialog_change_image, null)
        val builder = MaterialAlertDialogBuilder(requireContext()).create()
        builder.setView(changeImageLayout)
        builder.show()

        setImagePathOrUrl(
            changeImageLayout.findViewById(R.id.dialogImage),
            binding.viewModel?.userImageDocumentResidence?.value.orEmpty()
        )
        changeImageLayout.findViewById<Button>(R.id.submitButton).setOnClickListener {
            builder.cancel()
            openDialogDeleteImage()
        }
        changeImageLayout.findViewById<ImageView>(R.id.dialogImage).setOnClickListener {
            dispatchTakePictureIntent(REQUEST_RESIDENCE_PHOTO)
            builder.cancel()
        }
        changeImageLayout.findViewById<ImageView>(R.id.closeDialog)
            .setOnClickListener { builder.cancel() }
    }

    private fun openDialogDeleteImage() {
        val changeImageLayout = layoutInflater.inflate(R.layout.dialog_delete_image, null)
        val builder = MaterialAlertDialogBuilder(requireContext()).create()
        builder.setView(changeImageLayout)
        builder.show()

        changeImageLayout.findViewById<Button>(R.id.submitButton).setOnClickListener {
            binding.viewModel?.deleteProofResidenceImage()
            builder.cancel()
        }
        changeImageLayout.findViewById<Button>(R.id.dialogImage).setOnClickListener {
            builder.cancel()
            openDialogChangeImage()
        }
        changeImageLayout.findViewById<ImageView>(R.id.closeDialog).setOnClickListener {
            builder.cancel()
        }
    }

    private fun setupListeners() {
        binding.ietAge.addTextChangedListener(
            EditTextFormatMask.textMask(binding.ietAge, EditTextFormatMask.FORMAT_DATE)
        )

        binding.ietTelephone.addTextChangedListener(PhoneNumberFormattingTextWatcher("BR"))

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
            if (binding.viewModel?.userImageDocumentResidence?.value.isNullOrBlank()) {
                dispatchTakePictureIntent(REQUEST_RESIDENCE_PHOTO)
            } else {
                openDialogChangeImage()
            }
        }

        binding.etGender.setOnClickListener {
            createDialogGender()
        }

        binding.etSchooling.setOnClickListener {
            createDialogSchooling()
        }

        binding.etRacial.setOnClickListener {
            openDialogToSelectRace()
        }

        binding.ietIncome.setOnClickListener {
            openDialogToSelectIncome()
        }

        binding.schoolingStatusRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            binding.viewModel?.setSelectSchoolingStatusIndex(checkedId)
            binding.viewModel?.confirmUserSchoolingStatus()
        }
    }


}