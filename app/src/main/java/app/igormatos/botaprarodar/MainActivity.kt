package app.igormatos.botaprarodar

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    private val activitiesFragment: Fragment = ActivitiesFragment()
    private val usersFragment: Fragment = UsersFragment()
    private val bicycleFragment: Fragment = BicyclesFragment()
    val fm = supportFragmentManager
    var active = activitiesFragment

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                fm.beginTransaction().hide(active).show(activitiesFragment).commit()
                active = activitiesFragment
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_users -> {
                fm.beginTransaction().hide(active).show(usersFragment).commit()
                active = usersFragment
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_bicycles -> {
                fm.beginTransaction().hide(active).show(bicycleFragment).commit()
                active = bicycleFragment
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_dashboard -> {
                fm.beginTransaction().hide(active).show(bicycleFragment).commit()
                active = bicycleFragment
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.itemIconTintList = ColorStateList.valueOf(resources.getColor(R.color.tintColor))

        if (fm.fragments.isEmpty()) {
            fm.beginTransaction().add(R.id.main_container, bicycleFragment, "3").hide(bicycleFragment).commit()
            fm.beginTransaction().add(R.id.main_container, usersFragment, "2").hide(usersFragment).commit()
            fm.beginTransaction().add(R.id.main_container, activitiesFragment, "1").commit()
        }
    }
}
