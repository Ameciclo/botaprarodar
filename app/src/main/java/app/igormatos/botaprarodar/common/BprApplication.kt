package app.igormatos.botaprarodar.common

import android.app.Application
import io.realm.Realm

class BprApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
    }
}