package app.igormatos.botaprarodar.di

import app.igormatos.botaprarodar.data.network.firebase.*
import app.igormatos.botaprarodar.data.repository.FirebaseHelperRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.koin.dsl.module

val firebaseModule = module {
    single<FirebaseHelperModule> { FirebaseHelperModuleImpl() }
    single<FirebaseAuthModule> { FirebaseAuthModuleImpl() }
    single { FirebaseSessionManager(get(), get()) }
    single { FirebaseHelperRepository(get()) }
    single { FirebaseDatabase.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseAuth.getInstance() }
}