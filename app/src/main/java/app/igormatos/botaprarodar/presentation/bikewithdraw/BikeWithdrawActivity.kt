package app.igormatos.botaprarodar.presentation.bikewithdraw

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.databinding.ActivityBikeWithdrawBinding
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.BikeWithdrawViewModel
import app.igormatos.botaprarodar.presentation.components.FinishWithdraw
import org.koin.androidx.viewmodel.ext.android.viewModel

class BikeWithdrawActivity : AppCompatActivity() {

    private val navBuilder: NavOptions by lazy {
        NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()
    }

    private val navController: NavController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.bikeWithdrawNavHostFragment) as NavHostFragment
        navHostFragment.navController
    }

    private lateinit var binding: ActivityBikeWithdrawBinding
    private val viewModel: BikeWithdrawViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBikeWithdrawBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        setSupportActionBar(binding.toolbar)
        val rootView = binding.root
        setContentView(rootView)

        NavigationUI.setupWithNavController(binding.toolbar, navController)
        binding.bikeActionStepper.addItems(
            arrayListOf(
                StepConfigType.SELECT_BIKE,
                StepConfigType.SELECT_USER,
                StepConfigType.CONFIRM_WITHDRAW
            )
        )

        viewModel.uiState.observe(this) { stepConfigType ->
            when (stepConfigType) {
                StepConfigType.SELECT_BIKE -> navController.navigate(R.id.selectBike, null, navBuilder)
                StepConfigType.SELECT_USER -> navController.navigate(R.id.selectUser, null, navBuilder)
                StepConfigType.CONFIRM_WITHDRAW -> navController.navigate(R.id.confirmBikeSelection, null, navBuilder)
                else -> {
                    val intent = Intent(this, FinishWithdraw::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            binding.bikeActionStepper.setCurrentStep(stepConfigType)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onBackPressed() {
        if (viewModel.uiState.value == StepConfigType.SELECT_BIKE)
            finish()
        else
            binding.viewModel?.navigateToPrevious()
    }

    override fun finish() {
        super.finish()
        viewModel.backToInitialState()
    }
}
