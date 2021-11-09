package app.igormatos.botaprarodar.common.biding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.brunotmgomes.ui.extensions.loadPathOnCircle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

const val ROUND_CORNER_VALUE = 16


@BindingAdapter("imagePathOrUrl")
fun ImageView.setImagePathOrUrl(imagePathOrUrl: String) {
    Glide.with(this)
        .load(imagePathOrUrl)
        .apply(getRequestOptions(CenterCrop()))
        .into(this)
}

@BindingAdapter("imagePathOrUrlCircle")
fun ImageView.setImagePathOrUrlCircle(imagePathOrUrl: String) {
    this.loadPathOnCircle(imagePathOrUrl)
}

private fun getRequestOptions(transformation: BitmapTransformation) = RequestOptions()
    .transforms(transformation, RoundedCorners(ROUND_CORNER_VALUE))
    .diskCacheStrategy(DiskCacheStrategy.ALL)
