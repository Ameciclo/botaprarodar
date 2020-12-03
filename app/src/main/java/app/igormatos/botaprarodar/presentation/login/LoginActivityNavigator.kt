package app.igormatos.botaprarodar.presentation.login

import android.app.Activity
import android.content.Intent
import app.igormatos.botaprarodar.presentation.createcommunity.AddCommunityActivity
import app.igormatos.botaprarodar.presentation.main.MainActivity

open class LoginActivityNavigator {

    open fun goToMainActivity(activity: Activity){
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    open fun goToAddCommunityActivity(activity: Activity){
        val intent =
            Intent(activity, AddCommunityActivity::class.java)
        activity.startActivity(intent)
    }
}