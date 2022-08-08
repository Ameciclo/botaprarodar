package app.igormatos.botaprarodar.presentation.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType.USER_FORM
import app.igormatos.botaprarodar.common.enumType.StepConfigType.USER_QUIZ
import app.igormatos.botaprarodar.databinding.ActivityUserBinding
import app.igormatos.botaprarodar.domain.model.User
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class UserActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.returnNavHostFragment) }

    private val viewModel: UserViewModel by viewModel()

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user)
        binding.viewModel = viewModel

        viewModel.isEditableAvailable = intent.extras?.getParcelable<User>(USER_BUNDLE) != null

        setupNavGraph()

        setupStepperView()

        setupObservers()
    }

    private fun setupNavGraph() {
        navController.setGraph(R.navigation.user_nav_graph, intent.extras)
    }

    private fun setupStepperView() {
        binding.userActionStepper.addItems(
            arrayListOf(
                USER_FORM,
                USER_QUIZ
            )
        )
    }

    private fun setupObservers() {
        viewModel.uiState.observe(this) {
            binding.userActionStepper.setCurrentStep(it)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.navigateToPrevious()
    }

    override fun finish() {
        super.finish()
        viewModel.backToInitialState()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragment = supportFragmentManager.currentNavigationFragment

        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val USER_BUNDLE = "user"
        private const val COMMUNITY_USERS_BUNDLE = "communityUsers"

        fun setupActivity(
            context: Context,
            user: User? = null,
            currentCommunityUserList: ArrayList<User>
        ): Intent {
            val intent = Intent(context, UserActivity::class.java)
            val communityUsersArray = currentCommunityUserList.toTypedArray()
            intent.putExtra(COMMUNITY_USERS_BUNDLE, communityUsersArray)
            intent.putExtra(USER_BUNDLE, user)
            return intent
        }
    }
}

val FragmentManager.currentNavigationFragment: Fragment?
    get() = primaryNavigationFragment?.childFragmentManager?.fragments?.first()