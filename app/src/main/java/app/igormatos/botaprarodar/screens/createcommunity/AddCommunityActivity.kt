package app.igormatos.botaprarodar.screens.createcommunity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.util.isValidEmail
import app.igormatos.botaprarodar.databinding.ActivityAddCommunityBinding
import app.igormatos.botaprarodar.network.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_community.*
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class AddCommunityActivity : AppCompatActivity() {

    private lateinit var loadingDialog: AlertDialog
    private lateinit var viewBinding: ActivityAddCommunityBinding
    private val viewModel: AddCommunityViewModel by koinViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAddCommunityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.addCommunityButton.setOnClickListener {
            addCommunityEvent()
        }

        loadingDialog = MaterialAlertDialogBuilder(this)
            .setView(R.layout.loading_dialog_animation)
            .setCancelable(false)
            .create()

        observeViewModel()
    }

    private fun addCommunityEvent() {
        if (inputsFilled()) {
            createNewCommunity()
        } else {
            snackBarMaker(getString(R.string.empties_fields_error))
        }
    }

    private fun createNewCommunity() {
        viewBinding.let { view ->
            if (view.communityOrgEmailInput.text.isValidEmail()) {
                viewModel.createCommunity(
                    view.communityNameInput.text.toString(),
                    view.communityDescriptionInput.text.toString(),
                    view.communityAddressInput.text.toString(),
                    view.communityOrgNameInput.text.toString(),
                    view.communityOrgEmailInput.text.toString()
                )
            } else {
                snackBarMaker(getString(R.string.emailFormatWarning))
            }
        }
    }

    private fun inputsFilled(): Boolean {
        viewBinding.let { view ->
            return when {
                view.communityNameInput.text.isNullOrEmpty() -> false
                view.communityDescriptionInput.text.isNullOrEmpty() -> false
                view.communityAddressInput.text.isNullOrEmpty() -> false
                view.communityOrgNameInput.text.isNullOrEmpty() -> false
                view.communityOrgEmailInput.text.isNullOrEmpty() -> false
                else -> true
            }
        }
    }

    private fun observeViewModel() {
        viewModel.getLoadingValue().observe(this, Observer {
            if (it) loadingDialog.show() else loadingDialog.dismiss()
        })
        viewModel.getSuccessValue().observe(this, Observer {
            if (it)
                finish()
            else
                snackBarMaker(getString(R.string.add_community_error))
        })
        viewModel.getCommunityDataValue().observe(this, Observer {
            showConfirmationDialog(it)
        })
    }

    private fun showConfirmationDialog(community: Community) {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.community_confirm_title))
            .setMessage("${community.name} \n" +
                    "${community.description} \n" +
                    "${community.address} \n" +
                    "${community.org_name} \n" +
                    "${community.org_email}")
            .setPositiveButton(getString(R.string.community_add_confirm)) { _, _ ->
                viewModel.sendCommunityToServer()
            }.show()
    }

    private fun snackBarMaker(message: String) {
        Snackbar.make(
            addCommunityContainer,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }
}
