package app.igormatos.botaprarodar.presentation.welcome

import android.app.Activity
import android.content.Intent
import app.igormatos.botaprarodar.presentation.createcommunity.AddCommunityActivity
import app.igormatos.botaprarodar.presentation.main.MainActivity

open class WelcomeActivityNavigator {

    open fun goToMainActivity(activity: Activity) {
        val intent = MainActivity.getStartIntent(activity)
        activity.startActivity(intent)
        activity.finish()
    }

    open fun goToAddCommunityActivity(activity: Activity) {
        val intent =
            Intent(activity, AddCommunityActivity::class.java)
        activity.startActivity(intent)
    }
}