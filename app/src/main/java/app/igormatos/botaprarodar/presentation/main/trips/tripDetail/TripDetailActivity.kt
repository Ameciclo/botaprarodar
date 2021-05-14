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
import com.brunotmgomes.ui.SimpleResult
import com.brunotmgomes.ui.extensions.gone
import com.brunotmgomes.ui.extensions.loadPath
import com.brunotmgomes.ui.extensions.loadPathOnCircle
import com.brunotmgomes.ui.extensions.visible
import org.jetbrains.anko.backgroundColor
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        val isWithdraw = (args.bikeStatus.equals(getString(R.string.trip_withdraw), true))
        var devolution: Devolution? = null
        var withdraw: Withdraws? = null

        if (isWithdraw.not()) {
            devolution = viewModel.getDevolutionById(bike, args.id)
            withdraw = devolution?.withdrawId?.let { viewModel.getWithdrawById(bike, it) }
        } else {
            withdraw = viewModel.getWithdrawById(bike, args.id)
            devolution = viewModel.getDevolutionByWithdrawId(bike, args.id)
        }


        binding.apply {

            bike.photoThumbnailPath?.let { ivTripDetailBike.loadPath(it) }

            tvTripDetailBikeName.text = bike.name
            tvTripDetailBikeOrder.text = bike.orderNumber.toString()
            tvTripDetailBikeSeries.text = bike.serialNumber
            withdraw?.user?.profilePicture?.let { ivTripDetailUser.loadPathOnCircle(it) }
            tvTripDetailUserName.text = withdraw?.user?.name

            tvTripDetailWithdrawDate.text =
                getString(R.string.bike_withdraw_date, withdraw?.date)
            tvTripDetailReturnDate.text =
                getString(R.string.bike_devolution_date, devolution?.date)

            tvTripDetailStatus.apply {
                text = args.bikeStatus
                backgroundColor =
                    if (args.bikeStatus.equals(getString(R.string.trip_withdraw), true))
                        ContextCompat.getColor(context, R.color.yellow)
                    else
                        ContextCompat.getColor(context, R.color.bg_green)

            }

            if (viewModel.verifyIfBikeIsInUse(bike))
                btnTripDetailConfirm.visible()
            else
                btnTripDetailConfirm.gone()


        }

    }

}