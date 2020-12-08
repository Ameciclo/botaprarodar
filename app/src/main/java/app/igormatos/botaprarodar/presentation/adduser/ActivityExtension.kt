package app.igormatos.botaprarodar.presentation.adduser

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("imageUrl", "placeholder")
fun loadImage(view: ImageView, path: String, placeholder: Drawable) {
    with(view) {
        val requestOptions = RequestOptions()
            .placeholder(placeholder)
            .error(placeholder)
        Glide
            .with(context)
            .load(path)
            .apply(requestOptions)
            .apply(RequestOptions.fitCenterTransform())
            .into(this)
    }
}