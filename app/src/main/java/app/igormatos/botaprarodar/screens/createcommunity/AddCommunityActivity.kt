package app.igormatos.botaprarodar.screens.createcommunity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.ActivityAddCommunityBinding
import app.igormatos.botaprarodar.network.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_community.*
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class AddCommunityActivity : AppCompatActivity() {

    private lateinit var loadingDialog: AlertDialog
    private lateinit var binding: ActivityAddCommunityBinding
    private val viewModel: AddCommunityViewModel by koinViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_community)

        binding.viewModel = viewModel
        binding.community = viewModel.community

        loadingDialog = MaterialAlertDialogBuilder(this)
            .setView(R.layout.loading_dialog_animation)
            .setCancelable(false)
            .create()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loading.observe(this, Observer {
            if (it) loadingDialog.show() else loadingDialog.dismiss()
        })
        viewModel.success.observe(this, Observer {
            if (it)
                finish()
            else
                snackBarMaker(getString(R.string.add_community_error))
        })
        viewModel.inputFieldsWarning.observe(this, Observer {
            snackBarMaker(getString(R.string.empties_fields_error))
        })
        viewModel.communityData.observe(this, Observer {
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
