package app.igormatos.botaprarodar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_fullscreen_image.*

var EXTRA_IMAGE_PATH = "EXTRA_IMAGE_PATH"

class FullscreenImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_image)

        val imagePath = if (intent.hasExtra(EXTRA_IMAGE_PATH)) intent.getStringExtra(EXTRA_IMAGE_PATH) else null

        imagePath?.let { path ->
            imageView.loadPath(path)
        }

    }
}
