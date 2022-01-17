package app.igormatos.botaprarodar.common

import android.app.Application
import app.igormatos.botaprarodar.di.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

@ExperimentalCoroutinesApi
class BprApplication : Application() {

    private val modules = listOf(
        presentationModule,
        repositoryModule,
        viewModelModule,
        firebaseModule,
        useCasesModule,
        BPRModule,
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