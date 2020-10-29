package app.igormatos.botaprarodar.screens.fullscreenimage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.util.loadPath
import kotlinx.android.synthetic.main.activity_fullscreen_image.*


class FullscreenImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_image)

        val imagePath: String = intent.getStringExtra(extraImagePath)!!
        imageView.loadPath(imagePath)
    }

    companion object {

        private var extraImagePath = "EXTRA_IMAGE_PATH"

        @JvmStatic
        fun start(context: Context, imagePath: String? = "") {
            val starter = Intent(context, FullscreenImageActivity::class.java)
                .putExtra(extraImagePath, imagePath)
            context.startActivity(starter)
        }
    }
}
