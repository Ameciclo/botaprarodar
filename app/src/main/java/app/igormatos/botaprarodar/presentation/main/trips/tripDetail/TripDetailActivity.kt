package app.igormatos.botaprarodar.presentation.main.trips.tripDetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.extensions.getLastWithdraw
import app.igormatos.botaprarodar.databinding.ActivityTripDetailBinding
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.SimpleResult
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
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

        binding.apply {
            ivTripDetailBike.apply {
                Glide.with(context)
                    .load(bike.photoThumbnailPath)
                    .into(this)

            }

            tvTripDetailBikeName.text = bike.name
            tvTripDetailBikeOrder.text = bike.orderNumber.toString()
            tvTripDetailBikeSeries.text = bike.serialNumber

            val lastWithdraw = bike.getLastWithdraw()

            ivTripDetailUser.apply {
                val transforms = RequestOptions()
                    .transforms(CenterCrop(), RoundedCorners(16))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)

                Glide.with(context)
                    .load(lastWithdraw?.user?.profilePicture)
                    .apply(transforms)
                    .apply(RequestOptions().circleCrop())
                    .into(this)
            }

            tvTripDetailUserName.text = lastWithdraw?.user?.name
            tvTripDetailWithdrawDate.text =
                getString(R.string.bike_withdraw_date, lastWithdraw?.date)

            tvTripDetailStatus.apply {
                text = args.bikeStatus
                backgroundColor =
                    if (args.bikeStatus.equals(getString(R.string.trip_withdraw), true))
                        ContextCompat.getColor(context, R.color.yellow)
                    else
                        ContextCompat.getColor(context, R.color.bg_green)

            }

        }

    }

}