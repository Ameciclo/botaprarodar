package app.igormatos.botaprarodar.presentation.main.users

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.databinding.FragmentUsersBinding
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.adapter.UsersAdapter
import app.igormatos.botaprarodar.presentation.decoration.UserDecoration
import app.igormatos.botaprarodar.presentation.user.UserActivity
import com.brunotmgomes.ui.SimpleResult
import com.brunotmgomes.ui.extensions.snackBarMaker
import kotlinx.android.synthetic.main.fragment_users.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class UsersFragment : androidx.fragment.app.Fragment(), UsersAdapter.UsersAdapterListener {

    private val preferencesModule: SharedPreferencesModule by inject()
    private val usersAdapter = UsersAdapter(this)
    private lateinit var binding: FragmentUsersBinding
    private val usersViewModel: UsersViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_users, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        getUsers()
        observerUsers()
    }

    private fun initUI() {
        setupBtnRegisterClickEvent()
        setupSearchUsersEvent()
        setupRecyclerView()
    }

    private fun setupBtnRegisterClickEvent() {
        binding.btnRegisterUsers.setOnClickListener {
            val intent = UserActivity.setupActivity(requireContext())
            startForResult.launch(intent)
        }
    }

    private fun setupSearchUsersEvent() {
        binding.tieSearchUsers.addTextChangedListener {
            usersAdapter.filter.filter(it.toString())
        }
    }

    private fun setupRecyclerView() {
        val marginTop = resources.getDimensionPixelSize(R.dimen.padding_medium)
        rv_users.layoutManager = LinearLayoutManager(context)
        rv_users.adapter = usersAdapter
        rv_users.addItemDecoration(UserDecoration(marginTop))
    }

    private fun getUsers() {
        val joinedCommunityId = preferencesModule.getJoinedCommunity().id
        usersViewModel.getUsers(joinedCommunityId)
    }

    private fun observerUsers() {
        usersViewModel.users.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SimpleResult.Success -> {
                    usersAdapter.submitList(it.data)
                }
                is SimpleResult.Error -> {
                    showErrorMessage(getString(R.string.unkown_error))
                }
            }
        })
    }

    private fun showErrorMessage(errorMessage: String) {
        snackBarMaker(errorMessage, binding.btnRegisterUsers).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))
            show()
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                showSnackBar(result.data)
            }
        }

    private fun showSnackBar(intent: Intent?) {
        intent?.getBooleanExtra("isEditModeAvailable", false)?.let {
            val message = getSuccessMessage(it)
            snackBarMaker(message, requireView()).apply {
                setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.green))
                show()
            }
        }
    }

    private fun getSuccessMessage(isEditModeAvailable: Boolean) =
        if (isEditModeAvailable)
            getString(R.string.user_update_success)
        else
            getString(R.string.user_add_success)

    override fun onUserClicked(user: User) {
        val intent = UserActivity.setupActivity(requireContext(), user)
        startForResult.launch(intent)
    }
}