package app.igormatos.botaprarodar.presentation.adduser

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.TakePicture
import app.igormatos.botaprarodar.databinding.ActivityAddUserBinding
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.fullscreenimage.FullscreenImageActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_add_user.*
import org.jetbrains.anko.image
import org.parceler.Parcels
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class AddUserActivity : AppCompatActivity(R.layout.activity_add_user) {

    companion object {
        const val USER_EXTRA = "USER_EXTRA"
    }

    private var getPhoto = registerForActivityResult(TakePicture()) { photoWrapper ->
        viewModel.updatePhoto(photoWrapper)
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getPhoto.launch(requestCode)
        } else {
            showToast(R.string.need_permission_to_procede)
        }
    }

    private var requestCode: Int? = null
    private val viewModel: AddUserViewModel by koinViewModel()
    private lateinit var binding: ActivityAddUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)

        getUserIfExist()
        observeViewModel()
    }

    private fun getUserIfExist() {
        if (intent.hasExtra(USER_EXTRA)) {
            val userParcelable: Parcelable? = intent.getParcelableExtra(USER_EXTRA)
            viewModel.setupUserForEdit(Parcels.unwrap(userParcelable) as User)
        }
    }

    private fun observeViewModel() {
        viewModel.viewState.observe(this, { viewState ->
            when (viewState) {
                is AddUserViewState.ShowDialog -> {
                    showTipDialog(viewState.image, viewState.title, viewState.message) {
                        viewModel.callIntentTakePicture()
                    }
                }
                is AddUserViewState.TakePicture -> dispatchTakePictureIntent(viewState.code)
                is AddUserViewState.ShowToast -> showToast(viewState.message)
                is AddUserViewState.ShowLoading -> progressBar.visibility = View.VISIBLE
                is AddUserViewState.HideLoading -> progressBar.visibility = View.GONE
                is AddUserViewState.ImageFullScream -> {
                    FullscreenImageActivity.start(this, viewState.path)
                }
                else -> {
                    finish()
                }
            }
        })
    }

    private fun dispatchTakePictureIntent(code: Int) {
        this.requestCode = code
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            getPhoto.launch(code)
        } else {
            requestPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun showTipDialog(
        image: Int,
        title: Int,
        subtitle: Int,
        click: () -> Unit
    ) {
        val tipLayout = layoutInflater.inflate(R.layout.dialog_tip, null)

        tipLayout.findViewById<ImageView>(R.id.tipImage).image =
            ContextCompat.getDrawable(this, image)
        tipLayout.findViewById<TextView>(R.id.tipTitle).text = getString(title)
        tipLayout.findViewById<TextView>(R.id.tipSubtitle).text = getString(subtitle)

        MaterialAlertDialogBuilder(this)
            .setView(tipLayout)
            .setPositiveButton("Tirar foto!") { dialog, _ ->
                dialog.dismiss()
                click()
            }
            .show()
    }

    private fun showToast(message: Int) {
        Toast
            .makeText(this, message, Toast.LENGTH_SHORT)
            .show()
    }
}
