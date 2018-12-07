package app.igormatos.botaprarodar

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.fragment_list.*


class ActivitiesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)

        rootView.addItemFab.visibility = View.GONE
        rootView.speedDial.visibility = View.VISIBLE
        rootView.speedDial.inflate(R.menu.dial)

        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        speedDial.setOnActionSelectedListener { speedDialActionItem ->
            when (speedDialActionItem.id) {
                R.id.withdraw -> {
                    Toast.makeText(this@ActivitiesFragment.context, "Clicou", Toast.LENGTH_SHORT).show()
                    false // true to keep the Speed Dial open
                }
                else -> false
            }
        }
    }

}