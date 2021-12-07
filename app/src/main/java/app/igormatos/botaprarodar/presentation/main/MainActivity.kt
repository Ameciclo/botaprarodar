package app.igormatos.botaprarodar.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.databinding.ActivityMainBinding
import app.igormatos.botaprarodar.presentation.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    private val preferencesModule: SharedPreferencesModule by inject()

    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.mainToolbar)
        setupWithNavController()
    }

    private fun setupWithNavController() {
        binding.navigation.setupWithNavController(navController)
        binding.mainToolbar.setupWithNavController(navController, getAppBarConfiguration())
    }

    private fun getAppBarConfiguration(): AppBarConfiguration {
        return AppBarConfiguration(
            setOf(
                R.id.navigationHome,
                R.id.navigationUsers,
                R.id.navigationBicycles
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                preferencesModule.clear()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
