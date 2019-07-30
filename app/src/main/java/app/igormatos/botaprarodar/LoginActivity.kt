package app.igormatos.botaprarodar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.igormatos.botaprarodar.network.Community
import app.igormatos.botaprarodar.network.FirebaseHelper
import app.igormatos.botaprarodar.network.RequestError
import app.igormatos.botaprarodar.network.RequestListener
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return

        FirebaseHelper.getCommunities(currentUser.uid, object : RequestListener<List<Community>> {
            override fun onStart() {
                // loading
            }

            override fun onCompleted(result: List<Community>) {
                val communitiesTitle = result.mapNotNull { it.name }
                MaterialAlertDialogBuilder(this@LoginActivity)
                    .setTitle(title)
                    .setItems(communitiesTitle.toTypedArray()) { dialog, which ->
                        Toast.makeText(this@LoginActivity, "Dialog $dialog Int $which", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }

            override fun onError(error: RequestError) {

            }

        })


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
