package app.igormatos.botaprarodar.presentation.userForm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.ViewModelStatus
import app.igormatos.botaprarodar.common.components.CustomDialog
import app.igormatos.botaprarodar.common.components.CustomDialog.Companion.TAG
import app.igormatos.botaprarodar.databinding.ActivityAddUserBinding
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.CustomDialogModel
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.bikeForm.BikeFormActivity
import com.brunotmgomes.ui.extensions.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jetbrains.anko.image
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.parceler.Parcels

class UserFormActivity : AppCompatActivity() {

    private val binding: ActivityAddUserBinding by lazy {
        DataBindingUtil.setContentView<ActivityAddUserBinding>(this, R.layout.activity_add_user)
    }

    private val userFormViewModel: UserFormViewModel by viewModel()
    private var mCurrentPhotoPath = ""
    private var currentPhotoId = 0
    private lateinit var loadingDialog: AlertDialog

    companion object {
        private const val REQUEST_PROFILE_PHOTO = 1
        private const val REQUEST_ID_PHOTO = 2
        private const val REQUEST_RESIDENCE_PHOTO = 3
        private const val REQUEST_ID_PHOTO_BACK = 4
        const val USER_EXTRA = "USER_EXTRA"

        fun setupActivity(context: Context, user: User?): Intent {
            val intent = Intent(context, UserFormActivity::class.java)
            intent.putExtra(USER_EXTRA, user)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        binding.lifecycleOwner = this
        binding.viewModel = userFormViewModel
        loadingDialog = createLoading(R.layout.loading_dialog_animation)
        setupListeners()
        setupViewModelStatus()
        checkEditMode()
    }

    private fun checkEditMode() {
        if (intent.extras != null) {
            val userExtra = intent.extras?.getParcelable<User>(USER_EXTRA)
            setValuesToEditUser(userExtra)
        }
    }

    private fun setValuesToEditUser(user: User?) {
        user?.let { userFormViewModel.updateUserValues(it) }
    }

    private fun setupViewModelStatus() {
        binding.viewModel?.status?.observe(this, Observer {
            when (it) {
                is ViewModelStatus.Success -> {
                    loadingDialog.dismiss()
                    val intent = Intent().putExtra(
                        "isEditModeAvailable",
                        userFormViewModel.isEditableAvailable
                    )
                    setResult(RESULT_OK, intent)
                    finish()
                }
                is ViewModelStatus.Loading -> {
                    window.decorView.hideKeyboard()
                    loadingDialog.show()
                }
                is ViewModelStatus.Error -> {
                    snackBarMaker(it.message, binding.scrollContainer).apply {
                        setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.red))
                        show()
                    }
                    loadingDialog.dismiss()
                }
            }
        })

        binding.viewModel?.lgpd?.observe(this, Observer {
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
            }
            REQUEST_ID_PHOTO -> {
                binding.viewModel?.setDocumentImageFront(path)
                binding.tvAddFrontDocumentPhoto.gone()
            }
            REQUEST_ID_PHOTO_BACK -> {
                binding.viewModel?.setDocumentImageBack(path)
                binding.tvAddBackDocumentPhoto.gone()
            }
            REQUEST_RESIDENCE_PHOTO -> {
                binding.viewModel?.setResidenceImage(path)
                binding.tvAddResidencePhoto.gone()
            }
        }
    }

    private fun dispatchTakePictureIntent(code: Int) {
        takePictureIntent(code) { path ->
            mCurrentPhotoPath = path
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
            ContextCompat.getDrawable(this, image)
        tipLayout.findViewById<TextView>(R.id.tipTitle).text = title
        tipLayout.findViewById<TextView>(R.id.tipSubtitle).text = subtitle

        MaterialAlertDialogBuilder(this)
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

        CustomDialog.newInstance(dialogModel).show(supportFragmentManager, TAG)
    }
}