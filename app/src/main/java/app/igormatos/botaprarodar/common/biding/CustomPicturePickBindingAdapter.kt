package app.igormatos.botaprarodar.common.biding

import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.common.components.CustomPicturePick

@BindingAdapter("imagePathOrUrl")
fun CustomPicturePick.setImagePathOrUrl(imagePathOrUrl: String) {
    setUpPicture(imagePathOrUrl)
}
