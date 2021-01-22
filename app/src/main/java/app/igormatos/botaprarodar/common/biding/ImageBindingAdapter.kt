package app.igormatos.botaprarodar.common.biding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions


@BindingAdapter("app:imagePathOrUrl")
fun setImagePathOrUrl(imageView: ImageView, imagePathOrUrl: String) {
    Glide.with(imageView)
        .load(imagePathOrUrl)
        .apply(getRequestOptions())
        .into(imageView)
}

private fun getRequestOptions() = RequestOptions()
    .fitCenter()
    .error(R.drawable.ic_add_a_photo)
    .diskCacheStrategy(DiskCacheStrategy.ALL)