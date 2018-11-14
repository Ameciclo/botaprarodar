package app.igormatos.botaprarodar

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val fragment1: Fragment = ActivitiesFragment()
    private val fragment2: Fragment = UsersFragment()
    private val fragment3: Fragment = BicyclesFragment()
    val fm = supportFragmentManager
    var active = fragment1

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                fm.beginTransaction().hide(active).show(fragment1).commit()
                active = fragment1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                fm.beginTransaction().hide(active).show(fragment2).commit()
                active = fragment2
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                fm.beginTransaction().hide(active).show(fragment3).commit()
                active = fragment3
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (fm.fragments.isEmpty()) {
            fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit()
            fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit()
            fm.beginTransaction().add(R.id.main_container,fragment1, "1").commit()
        }
    }
}
