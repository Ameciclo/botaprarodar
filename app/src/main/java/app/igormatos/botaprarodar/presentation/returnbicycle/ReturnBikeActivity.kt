package app.igormatos.botaprarodar.presentation.returnbicycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.ActivityReturnBikeBinding

class ReturnBikeActivity : AppCompatActivity() {

    private val navController : NavController by lazy {
        findNavController(R.id.returnNavHostFragment)
    }

    private lateinit var binding: ActivityReturnBikeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReturnBikeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}