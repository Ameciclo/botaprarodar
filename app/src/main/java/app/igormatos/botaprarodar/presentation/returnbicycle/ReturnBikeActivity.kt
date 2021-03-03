package app.igormatos.botaprarodar.presentation.returnbicycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType.*
import app.igormatos.botaprarodar.databinding.ActivityReturnBikeBinding

class ReturnBikeActivity : AppCompatActivity() {

    private val navController: NavController by lazy {
        findNavController(R.id.returnNavHostFragment)
    }
    private val viewModel: ReturnBikeViewModel by lazy {
        ReturnBikeViewModel(StepperAdapter.ReturnStepper(SELECT_BIKE))
    }

    private lateinit var binding: ActivityReturnBikeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReturnBikeBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.returnBikeToolbar)
        NavigationUI.setupWithNavController(binding.returnBikeToolbar, navController)

        binding.bikeActionStepper.addItems(
            arrayListOf(
                SELECT_BIKE,
                QUIZ,
                CONFIRM_RETURN
            )
        )

        binding.next.setOnClickListener {
            viewModel.stepper.navigateToNext()
        }

        binding.previous.setOnClickListener {
            viewModel.stepper.navigateToPrevious()
        }

        binding.done.setOnClickListener {
            viewModel.stepper.navigateToNext()

        }


        viewModel.uiState.observe(this) {

            binding.bikeActionStepper
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}