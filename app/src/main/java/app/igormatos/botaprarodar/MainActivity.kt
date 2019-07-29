package app.igormatos.botaprarodar

import android.content.res.ColorStateList
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    private val activitiesFragment: androidx.fragment.app.Fragment = ActivitiesFragment()
    private val usersFragment: androidx.fragment.app.Fragment = UsersFragment()
    private val bicycleFragment: androidx.fragment.app.Fragment = BicyclesFragment()
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
