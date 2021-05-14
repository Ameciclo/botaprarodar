package app.igormatos.botaprarodar.presentation.returnbicycle

import android.content.Context
import android.content.Intent
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
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.bikeForm.BikeFormActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class ReturnBikeActivity : AppCompatActivity() {

    companion object {
        const val ORIGIN_FLOW = "origin flow"
        const val BIKE = "bike"

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

    private lateinit var binding: ActivityReturnBikeBinding
    private val viewModel: ReturnBikeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReturnBikeBinding.inflate(layoutInflater)

        setSupportActionBar(binding.returnBikeToolbar)
        val view = binding.root
        setContentView(view)

        NavigationUI.setupWithNavController(binding.returnBikeToolbar, navController)
        binding.bikeActionStepper.addItems(
            arrayListOf(
                StepConfigType.SELECT_BIKE,
                StepConfigType.QUIZ,
                StepConfigType.CONFIRM_DEVOLUTION
            )
        )

        viewModel.uiState.observe(this, Observer<StepConfigType> {
            binding.bikeActionStepper.setCurrentStep(it)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.navigateToPrevious()
    }

    override fun finish() {
        super.finish()
        viewModel.backToInitialState()
    }
}