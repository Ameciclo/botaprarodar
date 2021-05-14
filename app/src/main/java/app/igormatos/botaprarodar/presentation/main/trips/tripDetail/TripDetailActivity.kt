package app.igormatos.botaprarodar.presentation.main.trips.tripDetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.ActivityTripDetailBinding
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.Devolution
import app.igormatos.botaprarodar.domain.model.Withdraws
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeActivity
import com.brunotmgomes.ui.SimpleResult
import com.brunotmgomes.ui.extensions.gone
import com.brunotmgomes.ui.extensions.loadPath
import com.brunotmgomes.ui.extensions.loadPathOnCircle
import com.brunotmgomes.ui.extensions.visible
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.jetbrains.anko.backgroundColor
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class TripDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTripDetailBinding

    private val args: TripDetailActivityArgs by navArgs()

    private val viewModel by viewModel<TripDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTripDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getBikeById(args.bikeId)
        bikeObserver()
    }

    private fun bikeObserver() {
        viewModel.bike.observe(this, Observer {
            when (it) {
                is SimpleResult.Success -> {
                    setupTripDetailView(it.data)
                }
                is SimpleResult.Error -> {
                    // error
                }
            }
        })
    }

    private fun setupTripDetailView(bike: Bike) {
        val withdrawDevolutionPair = viewModel.returnWithdrawAndDevolution(
            status = args.bikeStatus,
            id = args.id,
            bike = bike
        )

        val withdraw = withdrawDevolutionPair.first
        val devolution = withdrawDevolutionPair.second

        binding.apply {
            setBikeAndUserInfos(bike, withdraw)
            setImagesValues(withdraw, bike)
            setDatesValues(withdraw, devolution)
            setBackgroundStatusColor()
            setDevolutionButtonVisibility(bike)
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
        bike: Bike
    ) {
        if (viewModel.verifyIfBikeIsInUse(bike)) {
            btnTripDetailConfirm.visible()
            btnTripDetailConfirm.setOnClickListener {
                val intent = ReturnBikeActivity.setupActivity(
                    context = this@TripDetailActivity,
                    originFlow = TRIP_DETAIL_FLOW,
                    bike = bike
                )
                startActivity(intent)
            }
        } else
            btnTripDetailConfirm.gone()
    }

    companion object {
        const val TRIP_DETAIL_FLOW = "TRIP_DETAIL"
    }

}