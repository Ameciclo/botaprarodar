package app.igormatos.botaprarodar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.igormatos.botaprarodar.local.Preferences
import app.igormatos.botaprarodar.local.model.Bicycle
import app.igormatos.botaprarodar.local.model.Item
import app.igormatos.botaprarodar.network.FirebaseHelper
import app.igormatos.botaprarodar.network.RequestListener
import kotlinx.android.synthetic.main.fragment_list.*

class BicyclesFragment : androidx.fragment.app.Fragment() {

    lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemAdapter = ItemAdapter(activity = activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addItemFab.setOnClickListener {
            val intent = Intent(it.context, AddBikeActivity::class.java)
            startActivity(intent)
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
        FirebaseHelper.getBicycles(joinedCommunityId, object : RequestListener<Bicycle> {
            override fun onChildChanged(result: Bicycle) {
                itemAdapter.updateItem(result)
            }

            override fun onChildAdded(result: Bicycle) {
                itemAdapter.addItem(result)
            }

            override fun onChildRemoved(result: Bicycle) {
                itemAdapter.removeItem(result)

            }

        })
    }
}