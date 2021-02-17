package app.igormatos.botaprarodar.presentation.main

import android.os.Bundle
import android.os.Parcelable
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.Item
import app.igormatos.botaprarodar.domain.model.Withdraw
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelper
import app.igormatos.botaprarodar.data.network.RequestListener
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.adapter.ItemAdapter
import app.igormatos.botaprarodar.presentation.bicyclewithdrawal.chooseuser.ChooseUserActivity
import app.igormatos.botaprarodar.presentation.returnbicycle.WITHDRAWAL_EXTRA
import kotlinx.android.synthetic.main.activity_choose_user.*
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.android.ext.android.inject
import org.parceler.Parcels


class UsersFragment : Fragment() {

    private val preferencesModule: SharedPreferencesModule by inject()

    lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemAdapter =
            ItemAdapter(activity = this.activity, onTouchUserItem = ::onTouchUserItem)

        addItemFab.setOnClickListener {
            onTouchUserItem(null)
        }

        arguments?.let {
            it.getParcelable<Parcelable>(WITHDRAWAL_EXTRA)?.let {
                addItemFab.visibility = View.GONE

                val withdrawal = Parcels.unwrap(it) as Withdraw
                itemAdapter.withdrawalInProgress = withdrawal
            }
        }

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter
        recyclerView.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                recyclerView.context,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )

        val joinedCommunityId = preferencesModule.getJoinedCommunity().id!!
        val filterOnlyAvailable = activity is ChooseUserActivity

        FirebaseHelper.getUsers(joinedCommunityId, false, object : RequestListener<Item> {
            override fun onChildChanged(result: Item) {
                itemAdapter.updateItem(result)
//                RealmHelper.insertUser(result as User)
            }

            override fun onChildAdded(result: Item) {
                itemAdapter.addItem(result)
//                RealmHelper.insertUser(result as User)
            }


            override fun onChildRemoved(result: Item) {
                itemAdapter.removeItem(result)
            }

        })

    }

    companion object {

        fun newInstance(withdraw: Withdraw): UsersFragment {
            val fragment =
                UsersFragment()
            val bundle = Bundle()
            bundle.putParcelable(WITHDRAWAL_EXTRA, Parcels.wrap(Withdraw::class.java, withdraw))
            fragment.arguments = bundle

            return fragment
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (activity is ChooseUserActivity) {
            val activityMenu = requireActivity().toolbar.menu
            val aMenuInflater = requireActivity().menuInflater

            activityMenu.clear()
            aMenuInflater.inflate(R.menu.search_users, menu)

            val searchView =
                SearchView((context as ChooseUserActivity).supportActionBar!!.themedContext)
            searchView.queryHint = "Busque pelo nome ou RG/CPF"

            activityMenu.findItem(R.id.action_search).apply {
                setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
                actionView = searchView
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    itemAdapter.filter.filter(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    itemAdapter.filter.filter(newText)
                    return true
                }
            })

            searchView.setOnClickListener { view -> }

        }
    }

    fun onTouchUserItem(user: User?) {
        val action = UsersFragmentDirections.actionUsersFragmentToAddUserFragment(user)
         findNavController().navigate(action)
    }
}