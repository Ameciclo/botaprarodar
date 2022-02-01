package app.igormatos.botaprarodar.presentation.main.trips.tripDetail

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.network.NoConnectionInterceptor
import app.igormatos.botaprarodar.databinding.ActivityTripDetailBinding
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.Devolution
import app.igormatos.botaprarodar.domain.model.Withdraws
import app.igormatos.botaprarodar.presentation.main.viewModel.TripDetailViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeActivity
import app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike.UiState
import com.brunotmgomes.ui.SimpleResult
import com.brunotmgomes.ui.extensions.*
import org.jetbrains.anko.backgroundColor
import org.koin.androidx.viewmodel.ext.android.viewModel

class TripDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTripDetailBinding

    private val args: TripDetailActivityArgs by navArgs()

    private val viewModel by viewModel<TripDetailViewModel>()

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.getBikeById(args.bikeId)
            }
        }

    private val loadingDialog: AlertDialog by lazy {
        createLoading(R.layout.loading_dialog_animation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTripDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getBikeById(args.bikeId)
        bikeObserver()
    }

    private fun bikeObserver() {
        viewModel.bike.observe(this, {
            when (it) {
                is SimpleResult.Success -> {
                    setupTripDetailView(it.data)
                }
                is SimpleResult.Error -> {
                    if (it.exception is NoConnectionInterceptor.NoConnectivityException) {
                        showErrorMessage(getString(R.string.connection_error))
                    } else {
                        showErrorMessage(getString(R.string.unkown_error))
                    }
                }
            }
        })

        viewModel.uiState.observe(this, { uiState ->
            when (uiState) {
                is UiState.Error -> loadingDialog.hide()
                UiState.Loading -> loadingDialog.show()
                UiState.Success -> loadingDialog.hide()
            }
        })
    }

    private fun setupTripDetailView(bike: Bike) {
        val withdrawDevolutionPair = viewModel.returnWithdrawAndDevolution(
            historicStatus = args.bikeStatus,
            withdrawOrDevolutionId = args.id,
            bike = bike
        )

        val withdraw = withdrawDevolutionPair.first
        val devolution = withdrawDevolutionPair.second

        binding.apply {
            setBikeAndUserInfos(bike, withdraw)
            setImagesValues(withdraw, bike)
            setDatesValues(withdraw, devolution)
            setBackgroundStatusColor()
            setDevolutionButtonClickEvent(bike)
            setDevolutionButtonVisibility(devolution)
        }

    }

    private fun ActivityTripDetailBinding.setBikeAndUserInfos(
        bike: Bike,
        withdraw: Withdraws?
    ) {
        tvTripDetailBikeName.text = bike.name
        tvTripDetailBikeOrder.text =
            getString(R.string.bike_order_number, bike.orderNumber.toString())
        tvTripDetailBikeSeries.text =
            getString(R.string.bike_series_number, bike.serialNumber)
        tvTripDetailUserName.text = withdraw?.user?.name
    }

    private fun ActivityTripDetailBinding.setImagesValues(
        withdraw: Withdraws?,
        bike: Bike
    ) {
        withdraw?.user?.profilePicture?.let { ivTripDetailUser.loadPathOnCircle(it) }
        bike.photoThumbnailPath?.let { ivTripDetailBike.loadPath(it) }
    }

    private fun ActivityTripDetailBinding.setDatesValues(
        withdraw: Withdraws?,
        devolution: Devolution?
    ) {
        tvTripDetailWithdrawDate.text =
            getString(R.string.bike_withdraw_date, withdraw?.date)
        tvTripDetailReturnDate.text =
            getString(
                R.string.bike_devolution_date,
                devolution?.date ?: getString(R.string.waiting_devolution)
            )
    }

    private fun ActivityTripDetailBinding.setBackgroundStatusColor() {
        tvTripDetailStatus.apply {
            text = args.bikeStatus
            backgroundColor =
                if (viewModel.verifyIfIsWithdraw(args.bikeStatus))
                    ContextCompat.getColor(context, R.color.yellow)
                else
                    ContextCompat.getColor(context, R.color.bg_green)

        }
    }

    private fun ActivityTripDetailBinding.setDevolutionButtonVisibility(
        devolution: Devolution?
    ) {
        if (viewModel.bikeWithdrawHasDevolution(devolution))
            btnTripDetailConfirm.gone()
        else
            btnTripDetailConfirm.visible()
    }

    private fun ActivityTripDetailBinding.setDevolutionButtonClickEvent(
        bike: Bike
    ) {
        btnTripDetailConfirm.setOnClickListener {
            val intent = ReturnBikeActivity.setupActivity(
                context = this@TripDetailActivity,
                originFlow = TRIP_DETAIL_FLOW,
                bike = bike
            )
            startForResult.launch(intent)
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        snackBarMaker(errorMessage, binding.cardView).apply {
            setBackgroundTint(ContextCompat.getColor(applicationContext, R.color.red))
            show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.cancel()
    }

    companion object {
        const val TRIP_DETAIL_FLOW = "TRIP_DETAIL"
    }

}