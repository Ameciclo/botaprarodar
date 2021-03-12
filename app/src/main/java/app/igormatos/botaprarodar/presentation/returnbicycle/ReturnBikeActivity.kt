package app.igormatos.botaprarodar.presentation.returnbicycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.databinding.ActivityReturnBikeBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class ReturnBikeActivity : AppCompatActivity() {

    private val navController: NavController by lazy {
        val navhosterFragment = supportFragmentManager
            .findFragmentById(R.id.returnNavHostFragment) as NavHostFragment
        navhosterFragment.navController
    }

    private lateinit var binding: ActivityReturnBikeBinding
    private val viewModel: ReturnBikeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReturnBikeBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.returnBikeToolbar)
        NavigationUI.setupWithNavController(binding.returnBikeToolbar, navController)

        binding.bikeActionStepper.addItems(
            arrayListOf(
                StepConfigType.SELECT_BIKE,
                StepConfigType.QUIZ,
                StepConfigType.CONFIRM_RETURN
            )
        )

        viewModel.uiState.observe(this, Observer<StepConfigType> {
            binding.bikeActionStepper.setCurrentStep(it)
            when (it) {
                StepConfigType.SELECT_BIKE -> {
                    navController.navigate(R.id.returnBikeFragment)
                }
                StepConfigType.QUIZ -> {
                    navController.navigate(R.id.returnBikeQuizFragment)
                }
                StepConfigType.CONFIRM_RETURN -> {
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onBackPressed() {
        if (viewModel.uiState.value == StepConfigType.SELECT_BIKE) {
            super.onBackPressed()
        } else {
            viewModel.navigateToPrevious()
        }
    }

    override fun finish() {
        super.finish()
        viewModel.stepper.backToInitialState()
    }
}