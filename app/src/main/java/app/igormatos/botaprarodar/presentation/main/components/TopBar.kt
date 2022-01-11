package app.igormatos.botaprarodar.presentation.main.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.presentation.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TopBar() {
    TopAppBar(
        title = { Text("Bota Pra Rodar") },
        actions = { LogoutButton() },
        backgroundColor = Color.White,
        modifier = Modifier.border(
            border = BorderStroke(0.dp, Color.Transparent),
            shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp)
        )
    )
}

@Composable
private fun LogoutButton() {
    val context = LocalContext.current
    IconButton(onClick = { logout(context = context) }) {
        Icon(imageVector = Icons.Filled.Logout, contentDescription = "Logout")
    }
}

fun logout(context: Context) {
    FirebaseAuth.getInstance().signOut()

    val preferencesModule = SharedPreferencesModule(context)
    preferencesModule.clear()

    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar()
}