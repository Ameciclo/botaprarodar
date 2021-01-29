package app.igormatos.botaprarodar.common.biding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions


@BindingAdapter("app:imagePathOrUrl", "app:errorDrawable")
fun setImagePathOrUrl(imageView: ImageView, imagePathOrUrl: String, errorDrawable: Drawable) {
    Glide.with(imageView)
        .load(imagePathOrUrl)
        .apply(getRequestOptions(errorDrawable))
        .into(imageView)
}

private fun getRequestOptions(errorImage: Drawable) = RequestOptions()
    .fitCenter()
    .error(errorImage)
    .diskCacheStrategy(DiskCacheStrategy.ALL)