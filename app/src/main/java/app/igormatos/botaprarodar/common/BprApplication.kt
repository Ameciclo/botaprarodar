package app.igormatos.botaprarodar.common

import android.app.Application
import app.igormatos.botaprarodar.di.bprModule
import app.igormatos.botaprarodar.di.useCasesModules
import app.igormatos.botaprarodar.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class BprApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BprApplication)
            modules(
                listOf(
                    bprModule,
                    useCasesModules,
                    viewModelModule
                )
            )
        }
    }

}