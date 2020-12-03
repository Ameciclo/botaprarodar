package app.igormatos.botaprarodar

import app.igormatos.botaprarodar.data.network.FirebaseAuthModule
import app.igormatos.botaprarodar.data.network.FirebaseAuthModuleImpl
import app.igormatos.botaprarodar.data.network.FirebaseHelperModule
import app.igormatos.botaprarodar.data.network.FirebaseHelperModuleImpl
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.domain.usecase.community.AddCommunityUseCase
import app.igormatos.botaprarodar.presentation.createcommunity.AddCommunityViewModel
import app.igormatos.botaprarodar.presentation.login.LoginActivityNavigator
import app.igormatos.botaprarodar.presentation.login.LoginActivityViewModel
import app.igormatos.botaprarodar.presentation.login.LoginActivityViewModelImpl
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