package app.igormatos.botaprarodar

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import androidx.appcompat.widget.SearchView
import app.igormatos.botaprarodar.local.Preferences
import app.igormatos.botaprarodar.local.model.Item
import app.igormatos.botaprarodar.local.model.Withdraw
import app.igormatos.botaprarodar.network.FirebaseHelper
import app.igormatos.botaprarodar.network.RequestListener
import kotlinx.android.synthetic.main.activity_choose_user.*
import kotlinx.android.synthetic.main.fragment_list.*
import org.parceler.Parcels


class UsersFragment : androidx.fragment.app.Fragment() {

    lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemAdapter = ItemAdapter(activity = this.activity)

        addItemFab.setOnClickListener {
            val intent = Intent(it.context, AddUserActivity::class.java)
            startActivity(intent)
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

        val joinedCommunityId = Preferences.getJoinedCommunity(context!!).id!!
        val filterOnlyAvailable = activity is ChooseUserActivity

        FirebaseHelper.getUsers(joinedCommunityId, filterOnlyAvailable, object : RequestListener<Item> {
            override fun onChildChanged(result: Item) {
                itemAdapter.updateItem(result)
            }

            override fun onChildAdded(result: Item) {
                itemAdapter.addItem(result)
            }


            override fun onChildRemoved(result: Item) {
                itemAdapter.removeItem(result)
            }

        })


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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (activity is ChooseUserActivity) {
            val activityMenu = activity!!.toolbar.menu
            val aMenuInflater = activity!!.menuInflater

            activityMenu.clear()
            aMenuInflater.inflate(R.menu.menu_main, menu)

            val searchView = SearchView((context as ChooseUserActivity).supportActionBar!!.themedContext)
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

}