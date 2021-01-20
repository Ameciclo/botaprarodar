package app.igormatos.botaprarodar.common.biding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


@BindingAdapter("app:imagePathOrUrl")
fun setImagePathOrUrl(imageView: ImageView, imagePathOrUrl: String) {
    if(imagePathOrUrl.isNotBlank()) {
        Glide.with(imageView)
            .load(imagePathOrUrl)
            .apply(RequestOptions.fitCenterTransform())
            .into(imageView)
    }
}