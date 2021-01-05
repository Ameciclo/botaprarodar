package app.igormatos.botaprarodar.presentation.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.ActivityAuthenticationBinding

class AuthenticationActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)

      val  navController = Navigation.findNavController(this, R.id.nav_host_fragment)
//        navController.
    }
}
