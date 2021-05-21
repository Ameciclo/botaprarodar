package app.igormatos.botaprarodar.presentation.welcome

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.ActivityWelcomeBinding
import app.igormatos.botaprarodar.domain.model.UserCommunityInfo
import app.igormatos.botaprarodar.presentation.authentication.AuthenticationActivity
import com.brunotmgomes.ui.SnackbarModule
import com.brunotmgomes.ui.extensions.createLoading
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.adapter_community.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class WelcomeActivity : AppCompatActivity(R.layout.activity_welcome) {

    private lateinit var chooseCommunityAdapter: CommunityAdapter

    private lateinit var views: ActivityWelcomeBinding
    private lateinit var loadingDialog: AlertDialog
    private lateinit var resendEmailSnackbar: Snackbar
    private var communityDialog: AlertDialog? = null

    private val viewModel: WelcomeActivityViewModel by koinViewModel()
    private val snackbarModule: SnackbarModule by inject()
    private val navigator: WelcomeActivityNavigator by inject()

    private val welcomeActivityResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        val resultCode = result.resultCode
        if (resultCode == Activity.RESULT_OK) {
            viewModel.onUserLoggedIn()
        } else {
            snackbarModule.make(
                views.loginContainer,
                getString(R.string.login_error),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views = ActivityWelcomeBinding.inflate(layoutInflater)
        val rootView = views.root
        setContentView(rootView)
        views.loginButton.setOnClickListener {
            startLoginFlow()
        }
        initScreenObjects()
        bindViewModel()
    }

    private fun initScreenObjects() {
        resendEmailSnackbar = snackbarModule.make(
            views.loginContainer,
            getString(R.string.login_confirm_email_error),
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(R.string.resend_email) {
                viewModel.sendEmailVerification()
            }
        }

        loadingDialog = createLoading(R.layout.loading_dialog_animation)

        chooseCommunityAdapter = CommunityAdapter(arrayListOf()) {
            viewModel.chooseCommunity(it)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkPreviousState()
    }

    private fun bindViewModel() {
        viewModel.navigateMain.observe(this) { event ->
            if (event.getContentIfNotHandled() != null) {
                navigator.goToMainActivity(this)
            }
        }
        viewModel.showResendEmailSnackBar.observe(this) { event ->
            val show = event.getContentIfNotHandled()
            if (show != null) {
                if (show) {
                    resendEmailSnackbar.show()
                } else {
                    resendEmailSnackbar.dismiss()
                }
            }
        }
        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) {
                loadingDialog.show()
            } else {
                if (loadingDialog.isShowing) {
                    loadingDialog.dismiss()
                }
            }
        }
        viewModel.loadedCommunities.observe(this) { event ->
            val content = event.getContentIfNotHandled()
            if (content != null) {
                if (content.isSuccess) {
                    showChooseCommunityDialog(content.getOrThrow())
                } else {
                    showCommunityErrorSnackbar()
                }
            }
        }
    }

    private fun inflateChooseCommunityDialogView(): View {
        val view = layoutInflater.inflate(R.layout.adapter_community, null)
        view.rvCommunityList.apply {
            adapter = chooseCommunityAdapter
        }
        return view
    }

    private fun showCommunityErrorSnackbar() {
        snackbarModule.make(
            views.loginContainer,
            getString(R.string.login_load_communities_error),
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(R.string.login_retry_load_community) {
                viewModel.retryLoadCommunities()
                dismiss()
            }
        }.show()
    }

    private fun showChooseCommunityDialog(userCommunityInfo: UserCommunityInfo) {
        communityDialog?.dismiss()
        if (!userCommunityInfo.isAdmin && userCommunityInfo.communities.isNullOrEmpty()) {
            communityDialog = showNoCommunitiesDialog()
        } else {
            val alertBuilder: MaterialAlertDialogBuilder =
                MaterialAlertDialogBuilder(this@WelcomeActivity)
                    .setTitle(title)
                    .setCancelable(false)
                    .setView(inflateChooseCommunityDialogView())

            if (userCommunityInfo.isAdmin) {
                alertBuilder.setPositiveButton(getString(R.string.add_community)) { _, _ ->
                    navigator.goToAddCommunityActivity(this@WelcomeActivity)
                }
            }

            chooseCommunityAdapter.updateList(userCommunityInfo.communities)
            communityDialog = alertBuilder.show()
        }
    }

    private fun showNoCommunitiesDialog(): AlertDialog {
        return MaterialAlertDialogBuilder(this@WelcomeActivity)
            .setTitle(title)
            .setMessage(getString(R.string.login_no_communities_allowed))
            .show()
    }

    private fun startLoginFlow() {
        welcomeActivityResultLauncher.launch(
            Intent(this, AuthenticationActivity::class.java)
        )
    }

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, WelcomeActivity::class.java)
        }
    }
}
