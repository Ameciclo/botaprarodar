package app.igormatos.botaprarodar.common.biding

import androidx.databinding.BindingAdapter
import app.igormatos.botaprarodar.common.components.CustomEditText

@BindingAdapter(value = ["app:textCaptured", "app:errorMessage"])
fun CustomEditText.setText(userCompleteName: String, errorMessage: String){
    this.setupText(userCompleteName, errorMessage)
}
