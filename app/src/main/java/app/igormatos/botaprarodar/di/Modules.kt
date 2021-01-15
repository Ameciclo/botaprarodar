package app.igormatos.botaprarodar.di

import app.igormatos.botaprarodar.BuildConfig
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.repository.CommunityRepository
import app.igormatos.botaprarodar.domain.model.community.CommunityMapper
import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.data.network.api.CommunityApiService
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModuleImpl
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelperModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelperModuleImpl

import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.data.repository.AdminRemoteDataSource
import app.igormatos.botaprarodar.presentation.authentication.EmailValidator
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.EmailValidationViewModel

import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.domain.usecase.bicycle.AddNewBikeUseCase
import app.igormatos.botaprarodar.domain.usecase.bicycle.BicyclesListUseCase
import app.igormatos.botaprarodar.domain.usecase.community.AddCommunityUseCase
import app.igormatos.botaprarodar.presentation.addbicycle.BikeFormViewModel

import app.igormatos.botaprarodar.presentation.createcommunity.AddCommunityViewModel
import app.igormatos.botaprarodar.presentation.login.LoginActivityNavigator
import app.igormatos.botaprarodar.presentation.login.LoginActivityViewModel
import app.igormatos.botaprarodar.presentation.login.LoginActivityViewModelImpl
import com.brunotmgomes.ui.SnackbarModule
import com.brunotmgomes.ui.SnackbarModuleImpl
import com.google.firebase.auth.FirebaseAuth
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

    viewModel {
        BikeFormViewModel(addNewBikeUseCase = get(), community = get<SharedPreferencesModule>().getJoinedCommunity())
    }

    single<BicycleApi> {
        get<Retrofit>().create(BicycleApi::class.java)
    }

    single {
        BikeRepository(get<BicycleApi>())
    }

    single {
        AddNewBikeUseCase(bikeRepository = get<BikeRepository>())
    }

    single {
        BicyclesListUseCase(get<BikeRepository>())
    }

    single {
        FirebaseAuth.getInstance()
    }


    single {
        AdminRemoteDataSource(get())
    }

    single {
        AdminRepository(get())
    }

    viewModel {
        EmailValidationViewModel(get(), EmailValidator())
    }

    viewModel {
        SignInViewModel(get(), PasswordValidator())
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