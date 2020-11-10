package app.igormatos.botaprarodar.screens.login

import android.app.Activity
import android.content.Intent
import app.igormatos.botaprarodar.screens.createcommunity.AddCommunityActivity
import app.igormatos.botaprarodar.screens.main.MainActivity

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