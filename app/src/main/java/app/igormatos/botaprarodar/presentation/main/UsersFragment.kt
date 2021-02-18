package app.igormatos.botaprarodar.presentation.main

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
import androidx.recyclerview.widget.LinearLayoutManager
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.RequestListener
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelperModule
import app.igormatos.botaprarodar.databinding.FragmentUsersBinding
import app.igormatos.botaprarodar.domain.model.Item
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.Withdraw
import app.igormatos.botaprarodar.presentation.UserDecoration
import app.igormatos.botaprarodar.presentation.UsersAdapter
import app.igormatos.botaprarodar.presentation.adduser.AddUserActivity
import app.igormatos.botaprarodar.presentation.returnbicycle.WITHDRAWAL_EXTRA
import com.brunotmgomes.ui.extensions.snackBarMaker
import kotlinx.android.synthetic.main.fragment_users.*
import org.koin.android.ext.android.inject
import org.parceler.Parcels

class UsersFragment : androidx.fragment.app.Fragment() {

    private val preferencesModule: SharedPreferencesModule by inject()
    private val firebaseHelper: FirebaseHelperModule by inject()
    lateinit var itemAdapter: UsersAdapter

    private lateinit var binding: FragmentUsersBinding

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
        itemAdapter = UsersAdapter()
        binding.btnRegisterUsers.setOnClickListener {
            val intent = Intent(it.context, AddUserActivity::class.java)
            startForResult.launch(intent)
        }
        setupRecyclerView()
        getUsers(preferencesModule.getJoinedCommunity().id)
        binding.tieSearchUsers.addTextChangedListener {
            itemAdapter.filter.filter(it.toString())
        }
    }

    private fun getUsers(joinedCommunityId: String) {
        firebaseHelper.getUsers(joinedCommunityId, false, object : RequestListener<Item> {
            override fun onChildChanged(result: Item) {
                itemAdapter.updateItem(result as User)
            }

            override fun onChildAdded(result: Item) {
                itemAdapter.addItem(result as User)
            }

            override fun onChildRemoved(result: Item) {
                itemAdapter.removeItem(result as User)
            }
        })
    }

    private fun setupRecyclerView() {
        val marginTop = resources.getDimensionPixelSize(R.dimen.padding_medium)
        rv_users.layoutManager = LinearLayoutManager(context)
        rv_users.adapter = itemAdapter
        rv_users.addItemDecoration(UserDecoration(marginTop))
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getStringExtra("successMessage")?.let {
                    snackBarMaker(it, requireView()).apply {
                        setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.green))
                        show()
                    }
                }
            }
        }

    companion object {
        fun newInstance(withdraw: Withdraw): UsersFragment {
            val fragment = UsersFragment()
            val bundle = Bundle()
            bundle.putParcelable(WITHDRAWAL_EXTRA, Parcels.wrap(Withdraw::class.java, withdraw))
            fragment.arguments = bundle
            return fragment
        }
    }
}