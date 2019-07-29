package app.igormatos.botaprarodar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {

    private val SIGN_IN_REQUEST: Int = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        if (FirebaseAuth.getInstance().currentUser == null) {
            startLoginFlow()
        } else {
            goToMainActivity()
        }

        loginButton.setOnClickListener {
            startLoginFlow()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                chooseCommunityDialog()
            } else {
//                erronologin snackbar
            }
        }
    }

    private fun chooseCommunityDialog() {
        val currentUser = FirebaseAuth.getInstance().currentUser

//        MaterialAlertDialogBuilder(this)
//            .setTitle("Comunidades")
//            .setMessage("Escolha qual comunidade entrar")
//            .setItems(arrayOf("Caranguejo Tabaiares", "Peixinhos")) { dialog, int ->
//                Toast.makeText(this@LoginActivity, "Dialog $dialog Int ${int}", Toast.LENGTH_SHORT).show()
//            }

//        goToMainActivity()

    }

    fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun startLoginFlow() {
        val providers = Arrays.asList(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.AppTheme)
                .build(),
            SIGN_IN_REQUEST
        )
    }

}
