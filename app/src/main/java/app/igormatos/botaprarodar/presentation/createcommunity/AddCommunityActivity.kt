package app.igormatos.botaprarodar.presentation.createcommunity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.ActivityAddCommunityBinding
import app.igormatos.botaprarodar.domain.model.community.CommunityRequest
import com.brunotmgomes.ui.extensions.createLoading
import com.brunotmgomes.ui.extensions.isValidEmail
import com.brunotmgomes.ui.extensions.showDialogMessage
import com.brunotmgomes.ui.extensions.snackBarMaker
import kotlinx.android.synthetic.main.activity_add_community.*
import java.time.LocalDate
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

        initLoadingDialogComponent()
        observeViewModel()
    }

    private fun addCommunityEvent() {
        if (inputsFilled()) saveNewCommunity()
        else snackBarMaker(getString(R.string.empties_fields_error), addCommunityContainer).show()
    }

    private fun saveNewCommunity() {
        if (validateEmailFormat()) showConfirmationDialog(createNewCommunity())
        else snackBarMaker(getString(R.string.email_format_warning), addCommunityContainer).show()
    }

    private fun validateEmailFormat(): Boolean {
        return viewBinding.communityOrgEmailInput.text.isValidEmail()
    }

    private fun createNewCommunity(): CommunityRequest {
        viewBinding.let { view ->
            return CommunityRequest(
                name = view.communityNameInput.text.toString(),
                description = view.communityDescriptionInput.text.toString(),
                address = view.communityAddressInput.text.toString(),
                orgName = view.communityOrgNameInput.text.toString(),
                orgEmail = view.communityOrgEmailInput.text.toString(),
                createdDate = LocalDate.now().toString(),
                id = null
            )
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

    private fun initLoadingDialogComponent() {
        loadingDialog = createLoading(R.layout.loading_dialog_animation)
    }

    private fun observeViewModel() {
        viewModel.getLoadingLiveDataValue().observe(this, Observer {
            if (it) loadingDialog.show() else loadingDialog.dismiss()
        })
        viewModel.getSuccessLiveDataValue().observe(this, Observer {
            if (it) finish()
            else snackBarMaker(
                getString(R.string.add_community_error),
                addCommunityContainer
            ).show()
        })
        viewModel.getErrorLiveDataValue().observe(this, Observer {
            snackBarMaker(getString(R.string.add_community_error), addCommunityContainer).show()
        })

    }

    private fun showConfirmationDialog(community: CommunityRequest) {
        showDialogMessage(
            title = getString(R.string.community_confirm_title),
            message = getCommunityMessage(community),
            isConfirmation = true,
            positiveButtonText = getString(R.string.community_add_confirm),
            positiveMethod = { viewModel.sendCommunity(community) }
        )
    }

    private fun getCommunityMessage(community: CommunityRequest) =
        "${community.name} \n${community.description} \n${community.address} \n${community.orgName} \n${community.orgEmail}"

    override fun onDestroy() {
        super.onDestroy()
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }
}
