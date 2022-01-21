package app.igormatos.botaprarodar.presentation.returnbicycle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.igormatos.botaprarodar.presentation.components.button.BackButtom
import app.igormatos.botaprarodar.presentation.returnbicycle.ui.theme.BotaprarodarTheme

class ReturnBicycleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BotaprarodarTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ReturnBicyclePage()
                }
            }
        }
    }
}

@Composable
fun ReturnBicyclePage() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            BackButtom {}
            Divider()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ReturnBicycleActivityPreview() {
    BotaprarodarTheme {
        ReturnBicyclePage()
    }
}