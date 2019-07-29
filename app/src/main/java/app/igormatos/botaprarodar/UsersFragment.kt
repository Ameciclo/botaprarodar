package app.igormatos.botaprarodar

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import android.view.*
import app.igormatos.botaprarodar.model.Item
import app.igormatos.botaprarodar.model.User
import app.igormatos.botaprarodar.model.Withdraw
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_choose_user.*
import kotlinx.android.synthetic.main.fragment_list.*
import org.parceler.Parcels


class UsersFragment : androidx.fragment.app.Fragment() {

    private val usersReference = FirebaseDatabase.getInstance().getReference("users")
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

        val usersListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val user = p0.getValue(User::class.java)
                itemAdapter.updateItem(user as Item)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val user = p0.getValue(User::class.java)
                itemAdapter.addItem(user as Item)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                itemAdapter.removeItem(user as Item)
            }
        }

        usersReference.addChildEventListener(usersListener)
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