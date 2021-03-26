package app.igormatos.botaprarodar.presentation.user.userform

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.ViewModelStatus
import app.igormatos.botaprarodar.common.components.CustomDialog
import app.igormatos.botaprarodar.common.components.CustomDialog.Companion.TAG
import app.igormatos.botaprarodar.databinding.FragmentUserFormBinding
import app.igormatos.botaprarodar.domain.model.CustomDialogModel
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.extensions.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jetbrains.anko.image
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFormFragment : Fragment() {

    private val args: UserFormFragmentArgs by navArgs()

    private val userFormViewModel: UserFormViewModel by viewModel()
    private var mCurrentPhotoPath = ""
    private var currentPhotoId = 0

    private lateinit var loadingDialog: AlertDialog

    private lateinit var binding: FragmentUserFormBinding

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
        binding = FragmentUserFormBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = userFormViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingDialog = requireContext().createLoading(R.layout.loading_dialog_animation)
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
        binding.tvAddResidencePhoto.gone()
        binding.tvAddBackDocumentPhoto.gone()
        binding.tvAddFrontDocumentPhoto.gone()
        binding.tvAddProfilePhoto.gone()
    }

    private fun setImageEditDescriptionsToVisible() {
        binding.ivEditProfilePhoto.visible()
        binding.ivEditResidencePhoto.visible()
        binding.ivEditFrontPhoto.visible()
        binding.ivEditBackPhoto.visible()
    }

    private fun setupViewModelStatus() {
        binding.viewModel?.status?.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ViewModelStatus.Success -> {
                    loadingDialog.dismiss()
                    val intent = Intent().putExtra(
                        "isEditModeAvailable",
                        userFormViewModel.isEditableAvailable
                    )
                    // TODO
                    //setResult(RESULT_OK, intent)
                    //finish()
                }
                is ViewModelStatus.Loading -> {
                    requireActivity().window.decorView.hideKeyboard()
                    loadingDialog.show()
                }
                is ViewModelStatus.Error -> {
                    snackBarMaker(it.message, binding.scrollContainer).apply {
                        setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))
                        show()
                    }
                    loadingDialog.dismiss()
                }
            }
        })

        binding.viewModel?.lgpd?.observe(viewLifecycleOwner, Observer {
            if (binding.viewModel?.isEditableAvailable == true) {
                binding.viewModel?.registerUser()
            } else if (it) {
                showConfirmDialog()
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
        // TODO
        //takePictureIntent(code) { path ->
        //    mCurrentPhotoPath = path
        //}
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
            .setPositiveButton("Tirar foto!") { _, _ ->
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
    }

    private fun showConfirmDialog() {
        val dialogModel = CustomDialogModel(
            title = getString(R.string.warning),
            message = getString(R.string.lgpd_message),
            primaryButtonText = getString(R.string.lgpd_confirm),
            primaryButtonListener = View.OnClickListener {
                binding.viewModel?.registerUser()
            }
        )

        CustomDialog.newInstance(dialogModel).show(childFragmentManager, TAG)
    }
}