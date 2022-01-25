package app.igormatos.botaprarodar.presentation.returnbicycle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import app.igormatos.botaprarodar.presentation.returnbicycle.ui.theme.BotaprarodarTheme

class ReturnBicycleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BotaprarodarTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ReturnBicyclePage(bikes = emptyList(), handleClick = {})
                }
            }
        }
    }
}
