package app.igormatos.botaprarodar.common

import android.app.Application
import app.igormatos.botaprarodar.di.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

@ExperimentalCoroutinesApi
class BprApplication : Application() {

    private val modules = listOf(
        firebaseModule,
        bprModule,
        viewModelModule,
        presentationModule,
        repositoryModule,
        useCasesModule,
        APIModule,
    )

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BprApplication)
            modules(modules = modules)
        }
    }
}