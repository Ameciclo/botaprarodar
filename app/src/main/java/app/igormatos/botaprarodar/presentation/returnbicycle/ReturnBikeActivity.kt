package app.igormatos.botaprarodar.presentation.returnbicycle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.databinding.ActivityReturnBikeBinding
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.main.trips.tripDetail.TripDetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalComposeUiApi
class ReturnBikeActivity : AppCompatActivity() {

    companion object {
        const val ORIGIN_FLOW = "origin flow"
        const val BIKE = "bike"
        const val SECOND_STEP = "QUIZ"

        fun setupActivity(context: Context, originFlow: String, bike: Bike? = null): Intent {
            val intent = Intent(context, ReturnBikeActivity::class.java)
            intent.putExtra(ORIGIN_FLOW, originFlow)
            intent.putExtra(BIKE, bike)
            return intent
        }
    }

    private val navController: NavController by lazy {
        val navhosterFragment = supportFragmentManager
            .findFragmentById(R.id.returnNavHostFragment) as NavHostFragment
        navhosterFragment.navController
    }
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var binding: ActivityReturnBikeBinding
    private val viewModel: ReturnBikeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReturnBikeBinding.inflate(layoutInflater)

        setSupportActionBar(binding.returnBikeToolbar)
        val view = binding.root
        setContentView(view)

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bikeActionStepper.addItems(
            arrayListOf(
                StepConfigType.SELECT_BIKE,
                StepConfigType.QUIZ,
                StepConfigType.CONFIRM_DEVOLUTION
            )
        )

        viewModel.uiState.observe(this) {
            when (it) {
                StepConfigType.SELECT_BIKE -> {
                    binding.returnBikeToolbar.let { toolbar ->
                        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
                        toolbar.setNavigationOnClickListener {
                            onBackPressed()
                        }
                    }
                    navController.navigate(R.id.returnBikeFragment)
                }
                StepConfigType.QUIZ -> navController.navigate(R.id.returnBikeQuizFragment)
                StepConfigType.CONFIRM_DEVOLUTION -> navController.navigate(R.id.stepFinalReturnBikeFragment)
                else -> {
                    val intent = Intent(this, FinishReturnBikeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            binding.bikeActionStepper.setCurrentStep(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        checkFlowOnBackPressed()

        if (flowIsDetailBikeActivityAndSecondStep(intent.getStringExtra(ORIGIN_FLOW)).not())
            viewModel.navigateToPrevious()

        return navController.navigateUp(appBarConfiguration)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        checkFlowOnBackPressed()
        viewModel.navigateToPrevious()
    }

    private fun checkFlowOnBackPressed() {
        val originFlow = intent.getStringExtra(ORIGIN_FLOW)
        if (flowIsDetailBikeActivityAndSecondStep(originFlow))
            finish()
    }

    private fun flowIsDetailBikeActivityAndSecondStep(originFlow: String?) =
        flowIsDetailBikeActivity(originFlow) && isSecondStep()

    private fun flowIsDetailBikeActivity(originFlow: String?) =
        originFlow.equals(TripDetailActivity.TRIP_DETAIL_FLOW, true)

    private fun isSecondStep() = viewModel.stepper.currentStep.value.name.equals(SECOND_STEP, true)
}