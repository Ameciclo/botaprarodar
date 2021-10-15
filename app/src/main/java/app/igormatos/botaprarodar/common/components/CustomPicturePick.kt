package app.igormatos.botaprarodar.common.components

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.biding.ImageBindingAdapter.setImagePathOrUrl
import app.igormatos.botaprarodar.databinding.CustomPicturePickBinding
import kotlinx.android.synthetic.main.activity_fullscreen_image.view.*
import kotlinx.android.synthetic.main.custom_picture_pick.view.*

class CustomPicturePick @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = CustomPicturePickBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        attrs?.let { attributes ->
            val typedArray = context.theme.obtainStyledAttributes(
                attributes,
                R.styleable.CustomPicturePick,
                0,
                0
            )
            with(binding) {
                val sdk = Build.VERSION.SDK_INT
                label.text = typedArray.getString(R.styleable.CustomPicturePick_android_text)
                if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                    icon.setBackgroundDrawable(typedArray.getDrawable(R.styleable.CustomPicturePick_android_src))
                } else {
                    icon.background = typedArray.getDrawable(R.styleable.CustomPicturePick_android_src)
                }
            }

        }
    }

    fun setUpPicture(imageUrlOrPath: String){
        imageUrlOrPath.takeIf {
            it.isNotBlank()
        }?.apply {
            setImagePathOrUrl(picture, imageUrlOrPath)
            setupIconAndLabelVisibility(INVISIBLE)
            setupImageAndLabelVisibility(VISIBLE)
        } ?: apply {
            setupIconAndLabelVisibility(VISIBLE)
            setupImageAndLabelVisibility(INVISIBLE)
        }
    }

    private fun setupIconAndLabelVisibility(value: Int){
        binding.apply {
            icon.visibility = value
            label.visibility = value
        }
    }

    private fun setupImageAndLabelVisibility(value: Int){
        binding.picture.visibility = value
    }

    fun setupClick(clickAction: () -> Unit){
        binding.picturePick.setOnClickListener {
            clickAction.invoke()
        }
    }

}