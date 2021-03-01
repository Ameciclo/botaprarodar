package app.igormatos.botaprarodar.common.biding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

private const val ROUND_CORNER_VALUE = 16

@BindingAdapter("app:imagePathOrUrl")
fun setImagePathOrUrl(imageView: ImageView, imagePathOrUrl: String) {
    Glide.with(imageView)
        .load(imagePathOrUrl)
        .apply(getRequestOptions())
        .into(imageView)
}

private fun getRequestOptions() = RequestOptions()
    .transforms(CenterCrop() ,RoundedCorners(ROUND_CORNER_VALUE))
    .diskCacheStrategy(DiskCacheStrategy.ALL)