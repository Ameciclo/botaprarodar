package app.igormatos.botaprarodar.screens.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.local.Preferences
import app.igormatos.botaprarodar.network.Community
import app.igormatos.botaprarodar.network.FirebaseHelper
import app.igormatos.botaprarodar.network.RequestError
import app.igormatos.botaprarodar.network.SingleRequestListener
import app.igormatos.botaprarodar.common.util.showLoadingDialog
import app.igormatos.botaprarodar.screens.createcommunity.AddCommunityActivity
import app.igormatos.botaprarodar.screens.main.MainActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {

    private val SIGN_IN_REQUEST: Int = 200
    lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            startLoginFlow()
        }
    }

    override fun onStart() {
        super.onStart()

        if (isLogged() && isCommunitySelected()) {
            goToMainActivity()
        } else if (isLogged()) {
            chooseCommunityDialog()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                chooseCommunityDialog()
            } else {
                Snackbar.make(loginContainer, getString(R.string.login_error), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun isCommunitySelected(): Boolean {
        return Preferences.isCommunitySelected(this)
    }

    private fun isLogged(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    private fun chooseCommunityDialog() {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return

        if (!currentUser.isEmailVerified) {
            val snackbar = Snackbar.make(
                loginContainer,
                getString(R.string.login_confirm_email_error),
                Snackbar.LENGTH_INDEFINITE
            )

            snackbar.setAction(getString(R.string.resend_email)) {
                currentUser.sendEmailVerification().addOnCompleteListener {
                    snackbar.dismiss()
                }
            }.show()

            return
        }

        FirebaseHelper.getCommunities(
            currentUser.uid,
            currentUser.email!!,
            object : SingleRequestListener<Pair<Boolean, List<Community>>> {
                override fun onStart() {
                    loadingDialog = showLoadingDialog()
                }

                override fun onCompleted(result: Pair<Boolean, List<Community>>) {
                    loadingDialog.dismiss()

                    val isAdmin = result.first
                    val communities = result.second

                    val communitiesTitle = communities.mapNotNull { it.name }

                    val alertBuilder = MaterialAlertDialogBuilder(this@LoginActivity)
                        .setTitle(title)
                        .setItems(communitiesTitle.toTypedArray()) { _, which ->
                            val joinedCommunity = communities[which]

                            Preferences.saveJoinedCommmunity(this@LoginActivity, joinedCommunity)
                            FirebaseHelper.setCommunityId(joinedCommunity.id!!)
                            goToMainActivity()
                        }

                    if (isAdmin) {
                        alertBuilder.setPositiveButton(getString(R.string.add_community)) { _, _ ->
                            val intent = Intent(this@LoginActivity, AddCommunityActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    alertBuilder.show()

                }

                override fun onError(error: RequestError) {
                    loadingDialog.dismiss()

                    MaterialAlertDialogBuilder(this@LoginActivity)
                        .setTitle(title)
                        .setMessage(getString(R.string.login_no_communities_allowed))
                        .show()

                    FirebaseAuth.getInstance().signOut()
                }

            })


    }

    fun goToMainActivity() {
        FirebaseHelper.setCommunityId(Preferences.getJoinedCommunity(this).id!!)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun startLoginFlow() {
        val providers = Arrays.asList(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.AppThemeWithActionbar)
                .build(),
            SIGN_IN_REQUEST
        )
    }

}
