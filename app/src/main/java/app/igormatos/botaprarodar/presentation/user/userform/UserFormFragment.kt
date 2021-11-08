package app.igormatos.botaprarodar.presentation.user.userform

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.biding.ImageBindingAdapter.setImagePathOrUrl
import app.igormatos.botaprarodar.common.utils.EditTextFormatMask
import app.igormatos.botaprarodar.databinding.DialogChangeImageBinding
import app.igormatos.botaprarodar.databinding.DialogDeleteImageBinding
import app.igormatos.botaprarodar.databinding.DialogTipBinding
import app.igormatos.botaprarodar.databinding.FragmentUserFormBinding
import app.igormatos.botaprarodar.domain.model.User
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
        setupViewModelStatus()
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
            if (it.schoolingStatus.isNullOrBlank()){
                binding.schoolingStatusRadioGroup.clearCheck()
            }
        }
    }

    private fun setupViewModelStatus() {
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

    }

    private fun updateViewModelLiveData(whichImageCode: Int, path: String) {
        when (whichImageCode) {
            REQUEST_PROFILE_PHOTO -> {
                binding.viewModel?.setProfileImage(path)
            }
            REQUEST_ID_PHOTO -> {
                binding.viewModel?.setDocumentImageFront(path)
            }
            REQUEST_ID_PHOTO_BACK -> {
                binding.viewModel?.setDocumentImageBack(path)
            }
            REQUEST_RESIDENCE_PHOTO -> {
                binding.viewModel?.setResidenceImage(path)
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

    private fun openDialogChangeImage() {
        val changeImageLayout = DialogChangeImageBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext()).create()
        builder.setView(changeImageLayout.root)
        builder.show()

        setImagePathOrUrl(
            changeImageLayout.dialogImage,
            binding.viewModel?.userImageDocumentResidence?.value.orEmpty()
        )
        changeImageLayout.submitButton.setOnClickListener {
            builder.cancel()
            openDialogDeleteImage()
        }
        changeImageLayout.dialogImage.setOnClickListener {
            dispatchTakePictureIntent(REQUEST_RESIDENCE_PHOTO)
            builder.cancel()
        }
        changeImageLayout.closeDialog.setOnClickListener { builder.cancel() }
    }

    private fun openDialogDeleteImage() {
        val changeImageLayout = DialogDeleteImageBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(requireContext()).create()
        builder.setView(changeImageLayout.root)
        builder.show()

        changeImageLayout.submitButton.setOnClickListener {
            binding.viewModel?.deleteProofResidenceImage()
            builder.cancel()
        }
        changeImageLayout.dialogImage.setOnClickListener {
            builder.cancel()
            openDialogChangeImage()
        }
        changeImageLayout.closeDialog.setOnClickListener {
            builder.cancel()
        }
    }

    private fun setupListeners() {
        binding.cetUserAge.addMask(
            EditTextFormatMask.FORMAT_DATE
        )

        binding.cetUserPhone.addEditTextListener(PhoneNumberFormattingTextWatcher("BR"))

        binding.cppPerfilPicture.setupClick {
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

        binding.cppDocumentFrontPicture.setupClick {
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

        binding.cppDocumentBackPicture.setupClick {
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

        binding.cppResidenceProofPicture.setupClick {
            if (binding.viewModel?.userImageDocumentResidence?.value.isNullOrBlank()) {
                dispatchTakePictureIntent(REQUEST_RESIDENCE_PHOTO)
            } else {
                openDialogChangeImage()
            }
        }

        binding.cstUserGender.setupClick {
            createDialogGender()
        }

        binding.cstUserSchooling.setupClick {
            createDialogSchooling()
        }

        binding.cstUserRacial.setupClick {
            openDialogToSelectRace()
        }

        binding.cstUserIncome.setupClick {
            openDialogToSelectIncome()
        }

        binding.schoolingStatusRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            binding.viewModel?.setSelectSchoolingStatusIndex(checkedId)
            binding.viewModel?.confirmUserSchoolingStatus()
        }
    }
}