package app.igormatos.botaprarodar

import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.*
import app.igormatos.botaprarodar.data.repository.CommunityRepository
import app.igormatos.botaprarodar.domain.model.community.CommunityMapper
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

    single { buildRetrofit() }

    single <CommunityApiService> {
        get<Retrofit>().create(CommunityApiService::class.java)
    }
    single { CommunityMapper() }
    single { CommunityRepository(
        communityApiService = get(),
        communityMapper = get()
    ) }
    single { AddCommunityUseCase(communityRepository = get()) }
    viewModel{
        AddCommunityViewModel(
            communityUseCase = get()
        )
    }

}

private fun buildRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}