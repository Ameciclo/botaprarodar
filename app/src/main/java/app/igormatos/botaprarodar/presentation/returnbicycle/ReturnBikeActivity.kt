package app.igormatos.botaprarodar.presentation.returnbicycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.databinding.ActivityReturnBikeBinding
import app.igormatos.botaprarodar.presentation.returnbicycle.stepOneBikes.StepOneBikesFragment
import app.igormatos.botaprarodar.presentation.returnbicycle.stepOneBikes.StepOneBikesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.androidx.scope.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.androidx.viewmodel.scope.viewModel
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import org.koin.core.qualifier.named
import org.koin.core.scope.KoinScopeComponent
import org.koin.core.scope.Scope
import org.koin.core.scope.get
import org.koin.core.scope.inject
import org.koin.experimental.property.inject
import org.koin.java.KoinJavaComponent.getKoin
import org.koin.java.KoinJavaComponent.inject

@ExperimentalCoroutinesApi
class ReturnBikeActivity : AppCompatActivity() {

    private val navController: NavController by lazy {
        findNavController(R.id.returnNavHostFragment)
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
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun finish() {
        super.finish()
        viewModel.stepper.backToInitialState()
    }
}