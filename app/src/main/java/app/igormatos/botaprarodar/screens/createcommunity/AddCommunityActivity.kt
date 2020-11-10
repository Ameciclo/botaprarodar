package app.igormatos.botaprarodar.screens.createcommunity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.network.Community
import app.igormatos.botaprarodar.network.FirebaseHelper
import app.igormatos.botaprarodar.network.RequestError
import app.igormatos.botaprarodar.network.SingleRequestListener
import app.igormatos.botaprarodar.common.util.showLoadingDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_add_community.*
import org.jetbrains.anko.childrenSequence
import org.jetbrains.anko.contentView

class AddCommunityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_community)

        addCommunityButton.setOnClickListener {
            if (!isInputsFilled()) {
                showRequiredFieldWarningDialog()
            } else {
                val community = getCommunityFromInputs()
                showConfirmationDialog(community)
            }
        }
    }

    private fun showConfirmationDialog(community: Community) {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.community_confirm_title))
            .setMessage(
                "${community.name} \n" +
                        "${community.description} \n" +
                        "${community.address} \n" +
                        "${community.org_name} \n" +
                        "${community.org_email}"
            )
            .setPositiveButton(getString(R.string.community_add_confirm)) { _, _ ->
                sendCommunityToServer(community)
            }
            .show()
    }

    private fun showRequiredFieldWarningDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Atenção!")
            .setMessage(R.string.empties_fields_error)
            .show()
    }

    private fun isInputsFilled(): Boolean {
        return when {
            communityNameInput.text.isNullOrEmpty() -> false
            communityDescriptionInput.text.isNullOrEmpty() -> false
            communityAddressInput.text.isNullOrEmpty() -> false
            communityOrgNameInput.text.isNullOrEmpty() -> false
            communityOrgEmailInput.text.isNullOrEmpty() -> false
            else -> true
        }
    }

    private fun sendCommunityToServer(community: Community) {
        val loadingDialog = showLoadingDialog()

        FirebaseHelper.addCommunity(community, object : SingleRequestListener<Boolean> {
            override fun onStart() {
            }

            override fun onCompleted(result: Boolean) {
                loadingDialog.dismiss()

                Snackbar.make(
                    addCommunityContainer,
                    getString(R.string.community_add_success_message),
                    Snackbar.LENGTH_SHORT
                ).show()

                finish()
            }

            override fun onError(error: RequestError) {
                loadingDialog.dismiss()
                Snackbar.make(
                    addCommunityContainer,
                    getString(R.string.add_community_error),
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun getCommunityFromInputs(): Community {
        val name = communityNameInput.text.toString()
        val description = communityDescriptionInput.text.toString()
        val address = communityAddressInput.text.toString()
        val orgName = communityOrgNameInput.text.toString()
        val orgEmail = communityOrgEmailInput.text.toString()

        return Community(name, description, address, orgName, orgEmail)
    }
}
