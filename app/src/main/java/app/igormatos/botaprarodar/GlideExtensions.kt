package app.igormatos.botaprarodar

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadPath(path: String) {
    Glide.with(context)
        .load(path)
        .apply(RequestOptions.fitCenterTransform())
        .into(this)
}