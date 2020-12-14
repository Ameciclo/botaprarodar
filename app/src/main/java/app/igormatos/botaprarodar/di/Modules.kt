package app.igormatos.botaprarodar.di

import app.igormatos.botaprarodar.BuildConfig
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.*
import app.igormatos.botaprarodar.data.repository.CommunityRepository
import app.igormatos.botaprarodar.domain.model.community.CommunityMapper
import app.igormatos.botaprarodar.data.network.*
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.usecase.community.AddCommunityUseCase
import app.igormatos.botaprarodar.domain.usecase.user.AddUserUseCase
import app.igormatos.botaprarodar.presentation.adduser.AddUserViewModel
import app.igormatos.botaprarodar.presentation.adduser.AddUserViewModelImpl
import app.igormatos.botaprarodar.presentation.createcommunity.AddCommunityViewModel
import app.igormatos.botaprarodar.presentation.login.LoginActivityNavigator
import app.igormatos.botaprarodar.presentation.login.LoginActivityViewModel
import app.igormatos.botaprarodar.presentation.login.LoginActivityViewModelImpl
import com.brunotmgomes.ui.SnackbarModule
import com.brunotmgomes.ui.SnackbarModuleImpl
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
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

    single<CommunityApiService> {
        get<Retrofit>().create(CommunityApiService::class.java)
    }
    single { CommunityMapper() }
    single {
        CommunityRepository(
            communityApiService = get(),
            communityMapper = get()
        )
    }
    single { AddCommunityUseCase(communityRepository = get()) }
    viewModel {
        AddCommunityViewModel(
            communityUseCase = get()
        )
    }

    single<BicycleApi> {
        get<Retrofit>().create(BicycleApi::class.java)
    }

    single { AddUserUseCase(userRepository = get()) }
    single {
        UserRepository(
            userApi = get(),
            preferencesModule = get()
        )
    }
    single<UserApiService> {
        get<Retrofit>().create(UserApiService::class.java)
    }

    viewModel<AddUserViewModel> {
        AddUserViewModelImpl(
            firebaseHelperModule = get(),
            userUseCase = get()
        )
    }
}

private fun buildRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}