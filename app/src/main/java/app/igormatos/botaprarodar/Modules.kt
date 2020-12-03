package app.igormatos.botaprarodar

import app.igormatos.botaprarodar.local.SharedPreferencesModule
import app.igormatos.botaprarodar.network.*
import app.igormatos.botaprarodar.screens.createcommunity.AddCommunityUseCase
import app.igormatos.botaprarodar.screens.createcommunity.AddCommunityViewModel
import app.igormatos.botaprarodar.screens.login.LoginActivityNavigator
import app.igormatos.botaprarodar.screens.login.LoginActivityViewModel
import app.igormatos.botaprarodar.screens.login.LoginActivityViewModelImpl
import com.brunotmgomes.ui.SnackbarModule
import com.brunotmgomes.ui.SnackbarModuleImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val bprModule = module {

    single { SharedPreferencesModule(appContext = get()) }
    single<FirebaseAuthModule> { FirebaseAuthModuleImpl() }
    single<FirebaseHelperModule> { FirebaseHelperModuleImpl() }
    single<SnackbarModule> { SnackbarModuleImpl() }

    single { LoginActivityNavigator() }
    viewModel<LoginActivityViewModel> {
        LoginActivityViewModelImpl(
            preferencesModule = get(),
            firebaseAuthModule = get(),
            firebaseHelperModule = get()
        )
    }

    single { AddCommunityUseCase(firebaseHelperModule = get()) }
    viewModel{
        AddCommunityViewModel(
            communityUseCase = get()
        )
    }

    single { buildRetrofit() }

}

private fun buildRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}